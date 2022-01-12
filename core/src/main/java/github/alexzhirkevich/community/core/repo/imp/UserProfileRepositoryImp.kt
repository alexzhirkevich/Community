package github.alexzhirkevich.community.core.repo.imp

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.SetOptions
import github.alexzhirkevich.community.core.await
import github.alexzhirkevich.community.core.repo.interfaces.FirebaseRepository
import github.alexzhirkevich.community.core.repo.interfaces.FirebaseRepository.Companion.FIELD_DESCRIPTION
import github.alexzhirkevich.community.core.repo.interfaces.FirebaseRepository.Companion.FIELD_IMAGE_URI
import github.alexzhirkevich.community.core.repo.interfaces.FirebaseRepository.Companion.FIELD_NAME
import github.alexzhirkevich.community.core.repo.interfaces.FirebaseRepository.Companion.FIELD_USERNAME
import github.alexzhirkevich.community.core.repo.interfaces.UserProfileRepository
import github.alexzhirkevich.community.core.repo.interfaces.UsersRepository
import javax.inject.Inject


class UserProfileRepositoryImp @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : UserProfileRepository {

    override suspend fun setName(id : String, name: String) {
        firebaseRepository.usersCollection
            .document(id)
            .set(mapOf(FIELD_NAME to name), SetOptions.merge())
            .await()

        FirebaseAuth.getInstance().currentUser?.updateProfile(
            UserProfileChangeRequest.Builder()
                .setDisplayName(name).build()
        )?.await()
    }

    override suspend fun setUsername(id : String, username: String) {
        firebaseRepository.usersCollection
            .document(id)
            .set(mapOf(FIELD_USERNAME to username), SetOptions.merge())
            .await()
    }

    override suspend fun setDescription(id : String, text: String) {
        firebaseRepository.usersCollection
            .document(id)
            .set(mapOf(FIELD_DESCRIPTION to text), SetOptions.merge())
            .await()
    }


    override suspend fun setImageUri(id : String, uri: String) {
        firebaseRepository.usersCollection
            .document(id)
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
