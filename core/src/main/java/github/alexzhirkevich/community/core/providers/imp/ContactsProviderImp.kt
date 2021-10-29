package github.alexzhirkevich.community.core.providers.imp

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.database.ContentObserver
import android.os.Handler
import android.os.Looper
import android.provider.ContactsContract
import android.util.Log
import com.google.firebase.firestore.CollectionReference
import github.alexzhirkevich.community.common.util.LTAG
import github.alexzhirkevich.community.core.*
import github.alexzhirkevich.community.core.entities.imp.ContactImpl
import github.alexzhirkevich.community.core.entities.interfaces.Contact
import github.alexzhirkevich.community.core.providers.interfaces.ContactsProvider
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider.Companion.COLLECTION_CONTACTS
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider.Companion.FIELD_PHONE
import github.alexzhirkevich.community.core.providers.interfaces.UsersProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalStdlibApi
@ExperimentalCoroutinesApi
@Singleton
class ContactsProviderImp @Inject constructor (
    private val contentResolver: ContentResolver,
    private val firebaseProvider: FirebaseProvider,
    private val usersProvider: UsersProvider,
) : ContactsProvider {

    private val contactsCollection : CollectionReference by lazy {
        firebaseProvider.usersCollection
            .document(usersProvider.currentUserId)
            .collection(COLLECTION_CONTACTS)
    }

    override suspend fun findByPhone(phone: String): Flow<Response<Contact>> =
        firebaseProvider.usersCollection
            .document(usersProvider.currentUserId)
            .collection(COLLECTION_CONTACTS)
            .whereEqualTo(FIELD_PHONE, phone)
            .limit(1)
            .asResponse<ContactImpl>()
            .map {
                if (it.isEmpty()){
                    Response.Error(SnapshotNotFoundException(
                            "Contact with phone $phone not found"))
                } else {
                    it.first()
                }
            }

    override fun getAll(limit: Int): Flow<Collection<Response<Contact>>> = callbackFlow {

        val contentObserver = object : ContentObserver(Handler(Looper.getMainLooper())) {
            @SuppressLint("Range")
            override fun onChange(selfChange: Boolean) {

                trySend(
                    read()
                    .filter { contact -> contact.phone.startsWith("+") }
                    .sorted()
                    .onEach { contact ->
                        contact.phone =
                            contact.phone.filter { phone -> phone.isDigit() || phone == '+' }
                    }.map {
                        Response.Success(
                            value = it,
                            isFromCache = true,
                            hasPendingWrites= false
                        )
                    }

                )
            }

            @SuppressLint("Range")
            fun read(): List<Contact> = buildList<Contact> {

                contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null
                ).use { cursor ->
                    while (cursor != null && cursor.moveToNext() &&
                        (size < limit || limit == -1)) {

                        val id = cursor.getString(
                            cursor.getColumnIndex(ContactsContract.Contacts._ID)
                        )
                        val hasPhone = cursor.getString(
                            cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)
                        ) == "1"

                        if (hasPhone) {
                            contentResolver.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                null,
                                null
                            ).use { phoneCursor ->
                                while (phoneCursor != null && phoneCursor.moveToNext() &&
                                    (size < limit || limit == -1)
                                ) {
                                    val number =
                                        phoneCursor.getString(
                                            phoneCursor.getColumnIndex(
                                                ContactsContract.CommonDataKinds.Phone.NUMBER
                                            )
                                        )
                                    val name =
                                        phoneCursor.getString(
                                            phoneCursor.getColumnIndex(
                                                ContactsContract.CommonDataKinds.Nickname.DISPLAY_NAME
                                            )
                                        )
                                    add(ContactImpl(name = name, phone = number))
                                }
                            }
                        }
                    }
                }
            }
        }

        contentResolver.registerContentObserver(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            true,
            contentObserver
        )

        awaitClose {
            contentResolver.unregisterContentObserver(contentObserver)
        }
    }

    override fun get(id: String): Flow<Response<Contact>> =
        contactsCollection
            .document(id)
            .asResponse<ContactImpl>()


    override suspend fun create(entity: Contact) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(entity: Contact) {
        TODO("Not yet implemented")
    }


    override suspend fun synchronize(contacts: Collection<Contact>) {
        contacts.map {
            contactsCollection.document(it.phone).set(it)
        }.awaitAll()
    }

    private suspend fun clear() {
        try {
            contactsCollection.delete()
        }catch (t : Throwable){
            Log.e(LTAG,"Failed to delete collection",t)
        }
    }
}
