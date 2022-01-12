package github.alexzhirkevich.community.core.repo.imp

import com.google.firebase.firestore.SetOptions
import github.alexzhirkevich.community.core.await
import github.alexzhirkevich.community.core.repo.interfaces.ChannelProfileRepository
import github.alexzhirkevich.community.core.repo.interfaces.FirebaseRepository
import github.alexzhirkevich.community.core.repo.interfaces.FirebaseRepository.Companion.FIELD_DESCRIPTION
import github.alexzhirkevich.community.core.repo.interfaces.FirebaseRepository.Companion.FIELD_IMAGE_URI
import github.alexzhirkevich.community.core.repo.interfaces.FirebaseRepository.Companion.FIELD_NAME
import github.alexzhirkevich.community.core.repo.interfaces.FirebaseRepository.Companion.FIELD_TAG
import javax.inject.Inject

class ChannelProfileRepositoryImp @Inject constructor(
        private val firebaseRepository: FirebaseRepository
) : ChannelProfileRepository {


    override suspend fun setName(id : String, name: String) {
        firebaseRepository.channelsCollection.document(id)
            .set(mapOf(FIELD_NAME to name), SetOptions.merge())
            .await()
    }

    override suspend fun setTag(id : String, tag: String) {
        firebaseRepository.channelsCollection.document(id)
            .set(mapOf(FIELD_TAG to tag), SetOptions.merge())
            .await()
    }

    override suspend fun setDescription(id : String, text: String) {
        firebaseRepository.channelsCollection.document(id)
            .set(mapOf(FIELD_DESCRIPTION to text), SetOptions.merge())
            .await()
    }

    override suspend fun setImageUri(id : String, uri: String) {
        firebaseRepository.channelsCollection.document(id)
            .set(mapOf(FIELD_IMAGE_URI to uri), SetOptions.merge())
            .await()
    }
}
