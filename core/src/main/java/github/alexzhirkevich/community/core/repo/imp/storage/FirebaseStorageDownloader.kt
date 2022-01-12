package github.alexzhirkevich.community.core.repo.imp.storage

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.google.firebase.storage.FirebaseStorage
import github.alexzhirkevich.community.core.Loading
import github.alexzhirkevich.community.core.SnapshotNotFoundException
import github.alexzhirkevich.community.core.asLoadingFlow
import github.alexzhirkevich.community.core.entities.Document
import github.alexzhirkevich.community.core.entities.MediaContent
import github.alexzhirkevich.community.core.entities.NetworkContent
import github.alexzhirkevich.community.core.entities.Voice
import github.alexzhirkevich.community.core.repo.interfaces.StorageDownloader
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.File
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.CoroutineContext

internal inline fun NetworkContent.toContentType() : ContentType<out NetworkContent>{
    return when (this){
        is MediaContent -> {
            when (type) {
                MediaContent.VIDEO -> ContentType.Video
                MediaContent.IMAGE -> ContentType.Image
                MediaContent.GIF -> ContentType.Gif
                else -> ContentType.Unknown
            }
        }
        is Document -> ContentType.Doc
        is Voice -> ContentType.Voice
        else -> ContentType.Unknown
    }
}

@ExperimentalCoroutinesApi
open class FirebaseStorageDownloader(scope: CoroutineScope, context: Context)
    : StorageDownloader, CoroutineScope {

    private val externalFilesDir = context.getExternalFilesDir(null)

    private val downloadStateMap = ConcurrentHashMap<String, InitializedLoadingEntry<out Uri>>()

    private fun findDownloadEntry(uri: String) : InitializedLoadingEntry<out Uri>?{
        return downloadStateMap[uri]
    }

    override fun isDownloaded(content : NetworkContent): Uri? {
        return kotlin.runCatching {
            getFile(content.url,content.toContentType(), false)?.toUri()
        }.getOrDefault(null)
    }


    override fun isDownloading(url: String): Boolean {
        return findDownloadEntry(url) != null
    }

    override val coroutineContext: CoroutineContext =
        newCoroutineContext(scope.coroutineContext)

    private fun initializeDownloadEntry(entry: LoadingEntry<Uri>)
            : InitializedLoadingEntry<Uri> {
        val stateFlow = MutableStateFlow<Loading<Uri>>(Loading.None)

        val initialized = InitializedLoadingEntry(
            uri = entry.uri,
            job = entry.flow.onEmpty {
                cancelDownload(entry.uri.path.orEmpty())
                if (currentCoroutineContext() !== entry.context) {
                    entry.context?.cancel()
                }
            }.onCompletion {
                cancelDownload(entry.uri.toString())
                if (currentCoroutineContext() !== entry.context) {
                    entry.context?.cancel()
                }
            }.catch {
            }.onEach {
                stateFlow.tryEmit(it)
            }.launchIn(this),
            flow = stateFlow.asStateFlow(),
            emit = stateFlow::tryEmit
        )

        downloadStateMap[entry.uri.path.orEmpty()] = initialized
        return initialized
    }

    override fun cancelDownload(url: String): Boolean =
        downloadStateMap[url]?.let {
            downloadStateMap.remove(url)
            it.emit(Loading.None)
            it.job?.cancel()
            true
        } ?: false


    override fun downloadImage(url: String) : StateFlow<Loading<Uri>> =
        download(url, ContentType.Image).flow

    override fun downloadVideo(url: String): StateFlow<Loading<Uri>> =
        download(url, ContentType.Video).flow

    override fun downloadVoice(url: String) : StateFlow<Loading<Uri>> =
        download(url, ContentType.Voice).flow


    @Throws(SnapshotNotFoundException::class, IOException::class)
    private fun getFile(
        url : String,
        content: ContentType<out NetworkContent>,
        create : Boolean) : File?{
        val name = try {
            FirebaseStorage.getInstance().getReferenceFromUrl(url).name
        } catch (t: Throwable) {
            throw SnapshotNotFoundException("File is not from storage")
        }

        val dir = File(externalFilesDir, content.directory)
        val file = File(dir,name)
        if (file.exists())
            return file

        if (!create){
            return null
        }
        dir.mkdirs()
        file.createNewFile()
        return file
    }

    private fun download(url : String, contentType: ContentType<out NetworkContent>)
            : InitializedLoadingEntry<out Uri> {
        try {
            findDownloadEntry(url)?.let {
                return it
            }

            var file = getFile(url,contentType,false)
            if (file != null){
                val flow = MutableStateFlow<Loading<Uri>>(Loading.Success(file.toUri()))

                return InitializedLoadingEntry(
                    uri = url.toUri(),
                    flow = flow.asStateFlow(),
                    emit = flow::tryEmit,
                )
            }

            file = getFile(url,contentType,true)!!

            val flow = FirebaseStorage.getInstance().getReferenceFromUrl(url)
                .getFile(file)
                .asLoadingFlow(file.toUri())

            val entry = LoadingEntry(
                uri = url.toUri(),
                flow = flow,
            )
            return initializeDownloadEntry(entry)
        }catch (t : Throwable){
            val flow = MutableStateFlow<Loading<Uri>>(Loading.Error(t))
            return InitializedLoadingEntry(
                uri = url.toUri(),
                flow = flow.asStateFlow(),
                emit = {
                    flow.tryEmit(it)
                }
            )
        }
    }

}
