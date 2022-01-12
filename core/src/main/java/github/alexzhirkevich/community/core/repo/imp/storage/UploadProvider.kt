package github.alexzhirkevich.community.core.repo.imp.storage

import android.net.Uri
import github.alexzhirkevich.community.core.Loading
import github.alexzhirkevich.community.core.entities.NetworkContent
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.concurrent.ConcurrentHashMap

internal interface UploadProvider {

    fun <T : NetworkContent> initializeUploadEntry(entry: LoadingEntry<T>):
            InitializedLoadingEntry<T>

    fun <T : NetworkContent> tryFindUploadEntry(uri: Uri) : InitializedLoadingEntry<T>?

    fun cancelUpload(path: Uri): Boolean
}

internal class UploadProviderImp(private val scope: CoroutineScope) : UploadProvider {

    private val uploadStateMap : MutableMap<Uri, InitializedLoadingEntry<out NetworkContent>> =
        ConcurrentHashMap()

    override fun cancelUpload(path: Uri): Boolean =
        uploadStateMap[path]?.let {
            uploadStateMap.remove(path)
            it.emit(Loading.None)
            it.job?.cancel()
            true
        } ?: false

    override fun <T : NetworkContent> initializeUploadEntry(entry: LoadingEntry<T>)
            : InitializedLoadingEntry<T> {

        tryFindUploadEntry<T>(entry.uri)?.let {
            return it
        }

        val stateFlow = MutableStateFlow<Loading<T>>(Loading.None)

        val initialized = InitializedLoadingEntry<T>(
            uri = entry.uri,
            job = entry.flow.onEmpty {
                cancelUpload(entry.uri)
                if (currentCoroutineContext() != entry.context) {
                    entry.context?.cancel()
                }
            }.onCompletion {
                cancelUpload(entry.uri)
                if (currentCoroutineContext() != entry.context) {
                    entry.context?.cancel()
                }
            }.catch {
            }.onEach {
                stateFlow.tryEmit(it)
            }.launchIn(scope  + (entry.context ?: Dispatchers.IO)),
            flow = stateFlow.asStateFlow(),
            emit = stateFlow::tryEmit
        )

        uploadStateMap[initialized.uri] = initialized
        return initialized
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : NetworkContent> tryFindUploadEntry(uri: Uri) = kotlin.runCatching {
        uploadStateMap[uri] as? InitializedLoadingEntry<T>
    }.onFailure {
        cancelUpload(uri)
    }.getOrNull()
}