package github.alexzhirkevich.community.core.repo.imp.storage

import android.content.Context
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.qualifiers.ApplicationContext
import github.alexzhirkevich.community.core.*
import github.alexzhirkevich.community.core.entities.*
import github.alexzhirkevich.community.core.entities.interfaces.Chat
import github.alexzhirkevich.community.core.entities.interfaces.Dialog
import github.alexzhirkevich.community.core.entities.interfaces.Group
import github.alexzhirkevich.community.core.entities.interfaces.User
import github.alexzhirkevich.community.core.repo.interfaces.FirebaseRepository.Companion.COLLECTION_CHANNELS
import github.alexzhirkevich.community.core.repo.interfaces.FirebaseRepository.Companion.COLLECTION_CHATS
import github.alexzhirkevich.community.core.repo.interfaces.FirebaseRepository.Companion.COLLECTION_USERS
import github.alexzhirkevich.community.core.repo.interfaces.FirebaseRepository.Companion.IMAGES
import github.alexzhirkevich.community.core.repo.interfaces.StorageChatUploader
import github.alexzhirkevich.community.core.repo.interfaces.StorageRepository
import github.alexzhirkevich.community.core.repo.interfaces.StorageUserUploader
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject


@ExperimentalCoroutinesApi
class StorageRepositoryImp @Inject constructor(
    scope : CoroutineScope,
    @ApplicationContext private val context : Context
    ) : FirebaseStorageDownloader(scope,context), StorageRepository {

    private val uploadProvider = UploadProviderImp(this)

    private fun createUploader(ref: StorageReference) = FirebaseStorageUploader(
            putReference = ref,
            uploadProvider = uploadProvider,
            coroutineScope = this@StorageRepositoryImp,
            context = context
        )

    override fun uploadForChat(chat: Chat) : StorageChatUploader =
        createUploader(createChatReference(chat))

    override fun uploadForUser(userId: String) : StorageUserUploader =
        createUploader(createUserReference(userId))

    override fun cancelUpload(path: Uri): Boolean =
        uploadProvider.cancelUpload(path)

    override suspend fun getUserAvatars(user: User, limit : Int): Collection<Uri> {
        val ref = createUserReference(user.id)
            .child(IMAGES)
        return list(ref, limit)
    }

    private fun createChatReference(chat: Chat) : StorageReference {
        val dir = when (chat) {
            is Group, is Dialog ->
                COLLECTION_CHATS
            else -> COLLECTION_CHANNELS
        }
        return FirebaseStorage.getInstance().reference
            .child(dir).child(chat.id)
    }

    private fun createUserReference(userId : String): StorageReference {
        return FirebaseStorage.getInstance()
            .reference.child(COLLECTION_USERS)
            .child(userId)
    }

    private suspend fun list(ref : StorageReference,limit: Int) : Collection<Uri> {
        val items = (if (limit>0)
            ref.list(limit)
        else ref.listAll()).await().items

        return items.map {
            it.downloadUrl
        }.awaitAll()
    }
}

