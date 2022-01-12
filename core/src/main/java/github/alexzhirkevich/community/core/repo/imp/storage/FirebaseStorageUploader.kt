package github.alexzhirkevich.community.core.repo.imp.storage

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.annotation.IntRange
import androidx.core.net.toFile
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import github.alexzhirkevich.community.core.Loading
import github.alexzhirkevich.community.core.Util
import github.alexzhirkevich.community.core.asLoadingFlow
import github.alexzhirkevich.community.core.entities.*
import github.alexzhirkevich.community.core.repo.interfaces.FirebaseRepository.Companion.FIELD_NAME
import github.alexzhirkevich.community.core.repo.interfaces.StorageChatUploader
import github.alexzhirkevich.community.core.repo.interfaces.StorageUserUploader
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.map
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
internal class FirebaseStorageUploader(
    private val putReference : StorageReference,
    private val uploadProvider: UploadProvider,
    private val coroutineScope: CoroutineScope,
    private val context : Context
) :
    StorageChatUploader,
    StorageUserUploader,
    CoroutineScope by coroutineScope {

    override fun uploadVideo(path: Uri): StateFlow<Loading<MediaContent>> {
        return uploadMediaContent(path, ContentType.Video)
    }

    override fun uploadImage(path: Uri): StateFlow<Loading<MediaContent>> {
        return uploadMediaContent(path, ContentType.Image)
    }

    override fun uploadAvatar(path: Uri): StateFlow<Loading<MediaContent>> =
        uploadImage(path)

    override fun uploadVoice(path: Uri): StateFlow<Loading<Voice>> {

        uploadProvider.tryFindUploadEntry<Voice>(path)?.let {
            return it.flow
        }

        val name = generateFileName()

        val localContext = Job(coroutineContext.job) + Dispatchers.IO

        val copy = async(localContext) {
            UploadUtil.copyToDownloads(context, path, ContentType.Voice.directory, name)
        }

        val intermediate = createVoiceInfo(context, path)

        return upload(path, name) {
            when (it) {
                is Loading.Success -> {
                    copy.await()
                    Loading.Success(
                        value = Voice(
                            url = it.value.toString(),
                            len =  intermediate.len
                        )
                    )
                }
                is Loading.Progress<*> -> {
                    Loading.Progress(
                        value = it.value,
                        totalBytes = it.totalBytes,
                        intermediate = intermediate
                    )
                }
                is Loading.Error -> {
                    copy.cancel()
                    UploadUtil.removeFromDownloads(context, ContentType.Voice.directory, name)
                    Loading.Error(it.error)
                }
                else -> Loading.None
            }
        }.flow
    }

    override fun uploadDocument(path: Uri): StateFlow<Loading<Document>> {
        uploadProvider.tryFindUploadEntry<Document>(path)?.let {
            return it.flow
        }

        val intermediate = createDocumentInfo(context, path)

        val name = generateFileName()
        return upload(path,name,null){
            when (it){
                is Loading.Success -> Loading.Success(
                    createDocumentInfo(context, it.value, path.lastPathSegment)
                )
                is Loading.Progress<*> -> Loading.Progress(it.value,it.totalBytes,intermediate)
                is Loading.Error -> Loading.Error(it.error)
                is Loading.None -> it
            }
        }.flow
    }

    private fun generateFileName() : String =
        System.currentTimeMillis().toString()+ Util.autoId(5)

    private fun uploadMediaContent(path: Uri, type: ContentType<MediaContent>)
        : StateFlow<Loading<MediaContent>>{

        uploadProvider.tryFindUploadEntry<MediaContent>(path)?.let {
            return it.flow
        }

        val name = generateFileName()
        val localContext = Job(coroutineContext.job) + Dispatchers.IO

        val intermediate = createMediaContentInfo(
            path,null, ContentType.Video
        )

        val thumbnail = async(localContext) {
            uploadThumbnailForMediaContent(
                context, path, type,"${name}_tn"
            )
        }

        val setThumbnailHandler = thumbnail.invokeOnCompletion {
            kotlin.runCatching {
                intermediate.thumbnailUrl = thumbnail.getCompleted()
            }
        }

        val copy = async(localContext) {
            UploadUtil.copyToDownloads(context, path, type.directory, name)
        }

        return upload(path, name,localContext) {
            when (it) {
                is Loading.Success -> {
                    thumbnail.await()
                    Loading.Success(
                        value = intermediate.apply {
                            url = it.toString()
                        }
                    )
                }
                is Loading.Progress<*> -> {
                    Loading.Progress(
                        value = it.value,
                        totalBytes = it.totalBytes,
                        intermediate = intermediate
                    )
                }
                is Loading.Error -> {
                    copy.cancel()
                    setThumbnailHandler.dispose()
                    thumbnail.cancel()
                    Loading.Error(it.error)
                }
                else -> Loading.None
            }
        }.flow
    }


    private fun <T : NetworkContent> upload(
        path: Uri,
        storageName : String,
        coroutineContext: CoroutineContext?=null,
        map: suspend (Loading<Uri>) -> Loading<T>
    ) : InitializedLoadingEntry<T> {
        val flow = putReference.child(ContentType.Voice.directory)
            .child(System.currentTimeMillis().toString() + Util.autoId(5))
            .child(storageName)
            .putFile(path, StorageMetadata.Builder()
                .setCustomMetadata(
                    FIELD_NAME, path.lastPathSegment ?: System.currentTimeMillis().toString()
                ).build())
            .asLoadingFlow()
            .map(map)

        val entry = LoadingEntry(
            uri = path,
            flow = flow,
            context = coroutineContext
        )

        return uploadProvider.initializeUploadEntry(entry)
    }

    private suspend fun uploadThumbnailForMediaContent(
        context: Context,
        contentPath: Uri,
        contentType: ContentType<MediaContent>,
        thumbnailName: String,
        ): String? {

        val uri = when (contentType) {
            ContentType.Image -> UploadUtil.createImageThumbnail(context, contentPath)
            ContentType.Video -> UploadUtil.createVideoThumbnail(context, contentPath)
            else -> null
        } ?: return null

        val last = putReference.child(ContentType.Voice.directory)
            .child(System.currentTimeMillis().toString() + Util.autoId())
            .child(thumbnailName)
            .putFile(uri)
            .asLoadingFlow()
            .lastOrNull()

        return (last as? Loading.Success<Uri>)?.value?.path
    }

    private companion object {

        private fun createMediaContentInfo(
            localPath: Uri,
            thumbnailUrl: String?,
            contentType: ContentType<MediaContent>,
            name : String? = localPath.lastPathSegment
            ) : MediaContent{

            val size = UploadUtil.getImageSize(localPath)

            return MediaContent(
                url = localPath.toString(),
                name = name ?: System.currentTimeMillis().toString(),
                thumbnailUrl = thumbnailUrl,
                type = if (contentType == ContentType.Image)
                    MediaContent.IMAGE else MediaContent.VIDEO,
                size = localPath.toFile().totalSpace,
                width = size?.width,
                height = size?.height
            )
        }

        private fun createVoiceInfo(
            context: Context,
            localPath: Uri,
        ) : Voice {
            return Voice(
                url = localPath.toString(),
                len = UploadUtil.getAudioLength(context, localPath) ?: 0
            )
        }

        @SuppressLint("Range")
        private fun createDocumentInfo(
            context: Context,
            localPath: Uri,
            name : String? = localPath.lastPathSegment
        ) : Document {
            return Document(
                type = getDocumentType(context, localPath),
                url = localPath.toString(),
                name = name ?: System.currentTimeMillis().toString(),
                size = localPath.toFile().totalSpace
            )
        }

        @IntRange(from = Document.TYPE_UNKNOWN, to = Document.TYPE_AUDIO)
        fun getDocumentType(context: Context, uri: Uri): Long {
            return when (UploadUtil.getFileExtension(context, uri)) {
                "png", "jpg", "jpeg", "bmp", "gif", "webp", "heic", "heif" -> Document.TYPE_IMAGE
                "mp3", "m4a", ".wav" -> Document.TYPE_AUDIO
                "mp4" -> Document.TYPE_VIDEO
                else -> Document.TYPE_UNKNOWN
            }
        }
    }
}

