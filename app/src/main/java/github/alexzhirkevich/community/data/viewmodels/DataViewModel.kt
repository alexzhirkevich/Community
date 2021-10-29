package github.alexzhirkevich.community.data.viewmodels

import android.util.Log
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import github.alexzhirkevich.community.BuildConfig
import github.alexzhirkevich.community.R
import github.alexzhirkevich.community.data.DataState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*


val Any.LTAG : String get() = this::class.java.simpleName

interface Updatable {
    fun update()
}

interface IDataViewModel<T> : Updatable {
    val data: State<DataState<T>>
    val isSynchronized: State<Boolean>
}

abstract class DataViewModel<T>(
) : ViewModel(), IDataViewModel<T>{


    @StringRes val defaultErrorMessage: Int
        = R.string.error_load_data

    final override val data: State<DataState<T>>
        get() = mData

    final override val isSynchronized: State<Boolean>
        get() = mIsSynchronized

    protected abstract val sourceFlow: Flow<DataState<T>>

    private val mIsSynchronized: MutableState<Boolean> = mutableStateOf(false)
    private val mData = mutableStateOf<DataState<T>>(DataState.Empty)
    private var job: Job? = null

    protected fun setSynchronized(isSyncronized: Boolean) {
        mIsSynchronized.value = isSyncronized
    }

    protected open fun translateError(throwable: Throwable) : DataState.Error
        = DataState.Error(defaultErrorMessage, throwable)

    @CallSuper
    override fun update() {
        job?.cancel()
        job = sourceFlow
            .onStart { emit(DataState.Loading) }
            .onEmpty { emit(DataState.Empty) }
            .onEach {
                mData.value = it
            }
            .catch { error ->
                if (error !is CancellationException) {
                    emit(translateError(error))
                    if (BuildConfig.DEBUG)
                        Log.e(
                            this@DataViewModel.LTAG,
                            "Error while collecting ViewModel data", error
                        )
                }
            }
            .launchIn(viewModelScope)
    }

}

@Composable
fun <T> Flow<DataState<T>>.collectAsState() =
    collectAsState(initial = DataState.Empty)

fun <T> Flow<DataState<T>>.asState(
    coroutineScope: CoroutineScope,
    error : (Throwable) -> DataState.Error,
) : State<DataState<T>> {
    val state = mutableStateOf<DataState<T>>(DataState.Empty)
    catch { err ->
        state.value = error(err)
    }
    onStart {
        state.value = DataState.Loading
    }
    launchIn(coroutineScope)

    return state
}
