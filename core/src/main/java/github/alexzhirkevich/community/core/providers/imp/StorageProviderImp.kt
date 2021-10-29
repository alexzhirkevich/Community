package github.alexzhirkevich.community.core.providers.imp

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import github.alexzhirkevich.community.core.Loading
import github.alexzhirkevich.community.core.asLoadingFlow
import github.alexzhirkevich.community.core.providers.interfaces.CurrentUserProvider
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider.Companion.IMAGES
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider.Companion.USER_DATA
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider.Companion.VOICES
import github.alexzhirkevich.community.core.providers.interfaces.StorageProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Singleton
class StorageProviderImp @Inject constructor(
        currentUserProvider: CurrentUserProvider
): StorageProvider {

    private val putReference = FirebaseStorage.getInstance().reference
        .child(USER_DATA)
        .child(currentUserProvider.currentUserId)

    override fun uploadImage(path: Uri): Flow<Loading<Uri>> {
//        val fileExtension = MimeTypeMap.getFileExtensionFromUrl(path.toString())
        return putReference
            .child(IMAGES)
            .child(System.currentTimeMillis().toString())
            .putFile(path)
            .asLoadingFlow()
    }

    override fun uploadVoice(path: Uri): Flow<Loading<Uri>> =
        putReference
            .child(VOICES)
            .child("${System.currentTimeMillis()}.mp3")
            .putFile(path)
            .asLoadingFlow()
}

