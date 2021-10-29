package github.alexzhirkevich.community.core.providers.imp

import com.google.firebase.firestore.SetOptions
import github.alexzhirkevich.community.core.await
import github.alexzhirkevich.community.core.providers.interfaces.ChannelProfileProvider
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider.Companion.FIELD_DESCRIPTION
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider.Companion.FIELD_IMAGE_URI
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider.Companion.FIELD_NAME
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider.Companion.FIELD_TAG
import javax.inject.Inject

class ChannelProfileProviderImp @Inject constructor(
        private val id : String,
        private val firebaseProvider: FirebaseProvider
) : ChannelProfileProvider {

    private val doc = firebaseProvider.channelsCollection.document(id)

    override suspend fun setName(name: String) {
        doc.set(mapOf(FIELD_NAME to name), SetOptions.merge())
            .await()
    }

    override suspend fun setTag(tag: String) {
        doc.set(mapOf(FIELD_TAG to tag), SetOptions.merge())
            .await()
    }

    override suspend fun setDescription(text: String) {
        doc.set(mapOf(FIELD_DESCRIPTION to text), SetOptions.merge())
            .await()
    }

    override suspend fun setImageUri(uri: String) {
        doc.set(mapOf(FIELD_IMAGE_URI to uri), SetOptions.merge())
            .await()
    }
}
