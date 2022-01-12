package github.alexzhirkevich.community.core.repo.interfaces

import android.content.Context
import android.net.Uri
import github.alexzhirkevich.community.core.Loading
import github.alexzhirkevich.community.core.entities.Document
import github.alexzhirkevich.community.core.entities.MediaContent
import github.alexzhirkevich.community.core.entities.NetworkContent
import github.alexzhirkevich.community.core.entities.Voice
import github.alexzhirkevich.community.core.entities.interfaces.Chat
import github.alexzhirkevich.community.core.entities.interfaces.User
import github.alexzhirkevich.community.core.repo.base.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow


interface StorageChatUploader {
    fun uploadImage(path: Uri) : StateFlow<Loading<MediaContent>>
    fun uploadVoice(path: Uri) : StateFlow<Loading<Voice>>
    fun uploadVideo(path: Uri) : StateFlow<Loading<MediaContent>>
    fun uploadDocument(path: Uri) : StateFlow<Loading<Document>>
}

interface StorageUserUploader {
    fun uploadAvatar(path: Uri) : StateFlow<Loading<MediaContent>>
}

interface StorageDownloader {

    fun isDownloaded(content: NetworkContent) : Uri?

    fun isDownloading(url: String) : Boolean

    fun cancelDownload(url: String) : Boolean

    fun downloadImage(url : String) : StateFlow<Loading<Uri>>
    fun downloadVoice(url: String): StateFlow<Loading<Uri>>
    fun downloadVideo(url: String): StateFlow<Loading<Uri>>
}

interface StorageRepository : Repository, StorageDownloader {

    fun uploadForChat(chat: Chat) : StorageChatUploader
    fun uploadForUser(userId: String) : StorageUserUploader

    fun cancelUpload(path: Uri) : Boolean

    suspend fun getUserAvatars(user: User,limit : Int = 30) : Collection<Uri>

//    suspend fun getChatImages(chat: Chat, limit : Int = 30) : Collection<MediaContent>
//    suspend fun getChatVideos(chat: Chat, limit : Int = 30) : Collection<MediaContent>
//    suspend fun getChatVoices(chat: Chat, limit : Int = 30) : Collection<Voice>
}