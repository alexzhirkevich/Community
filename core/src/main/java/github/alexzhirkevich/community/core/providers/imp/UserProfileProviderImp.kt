package github.alexzhirkevich.community.core.providers.imp

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.SetOptions
import github.alexzhirkevich.community.core.await
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider.Companion.FIELD_DESCRIPTION
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider.Companion.FIELD_IMAGE_URI
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider.Companion.FIELD_NAME
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider.Companion.FIELD_USERNAME
import github.alexzhirkevich.community.core.providers.interfaces.UserProfileProvider
import github.alexzhirkevich.community.core.providers.interfaces.UsersProvider
import javax.inject.Inject


class UserProfileProviderImp @Inject constructor(
        private var usersProvider: UsersProvider,
        private val firebaseProvider: FirebaseProvider
) : UserProfileProvider {

    override suspend fun setName(name: String) {
        firebaseProvider.usersCollection
            .document(usersProvider.currentUserId)
            .set(mapOf(FIELD_NAME to name), SetOptions.merge())
            .await()

        FirebaseAuth.getInstance().currentUser?.updateProfile(
            UserProfileChangeRequest.Builder()
                .setDisplayName(name).build()
        )?.await()
    }

    override suspend fun setUsername(username: String) {
        firebaseProvider.usersCollection
            .document(usersProvider.currentUserId)
            .set(mapOf(FIELD_USERNAME to username), SetOptions.merge())
            .await()
    }

    override suspend fun setDescription(text: String) {
        firebaseProvider.usersCollection
            .document(usersProvider.currentUserId)
            .set(mapOf(FIELD_DESCRIPTION to text), SetOptions.merge())
            .await()
    }


    override suspend fun setImageUri(uri: String) {
        firebaseProvider.usersCollection
            .document(usersProvider.currentUserId)
            .set(mapOf(FIELD_IMAGE_URI to uri), SetOptions.merge())
            .await()

        FirebaseAuth.getInstance().currentUser
            ?.updateProfile(
                UserProfileChangeRequest.Builder()
                    .setPhotoUri(Uri.parse(uri)).build()
            )?.await()
    }

//    override fun getContacts(): Observable<List<IUser>> =
//            FirebaseDatabase.getInstance().reference.child(USERS).child(usersProvider.currentUserId)
//                    .child(FirebaseUtil.CONTACTS).toObservable {
//                        it.value as Map<String, String>
//                    }.map { usersProvider.findByPhone(*it.keys.toTypedArray()) }.concatAll()
//
//
//    override fun setContacts(contacts: Map<String, String>): Completable =
//            FirebaseDatabase.getInstance().reference.child(USERS).child(usersProvider.currentUserId)
//                    .child(FirebaseUtil.CONTACTS).setValue(contacts).toCompletable()




}
