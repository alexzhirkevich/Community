package github.alexzhirkevich.community.core.repo.imp.storage

import android.net.Uri
import github.alexzhirkevich.community.core.Loading
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext

data class LoadingEntry<T> (
    val uri : Uri,
    val flow: Flow<Loading<T>>,
    val context : CoroutineContext?=null
)

data class InitializedLoadingEntry<T>(
    val uri : Uri,
    val flow : StateFlow<Loading<T>>,
    val emit : (Loading<T>) ->Unit,
    val job : Job?=null,
)