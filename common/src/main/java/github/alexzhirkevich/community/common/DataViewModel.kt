package github.alexzhirkevich.community.common

import android.util.Log
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import github.alexzhirkevich.community.common.util.LTAG
import github.alexzhirkevich.community.core.logger.Logger
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*



interface Updatable {
    fun update()
}

interface IDataViewModel<T> : Updatable {
    val data: StateFlow<DataState<T>>
    val isSynchronized: StateFlow<Boolean>
}

abstract class DataViewModel<T>(val logger : Logger?=null) : ViewModel(), IDataViewModel<T>{


    @StringRes val defaultErrorMessage: Int
        = R.string.error_load_data
    private val mData = MutableStateFlow<DataState<T>>(DataState.Empty)
    final override val data: StateFlow<DataState<T>> = mData.asStateFlow()

    private val mIsSynchronized =  MutableStateFlow(false)
    final override val isSynchronized: StateFlow<Boolean> = mIsSynchronized.asStateFlow()

    protected abstract val sourceFlow: Flow<DataState<T>>

    private var job: Job? = null

    protected fun setSynchronized(isSyncronized: Boolean) {
        mIsSynchronized.value = isSyncronized
    }


    protected open fun translateError(throwable: Throwable) : DataState.Error =
        DataState.Error(defaultErrorMessage, throwable)


    @CallSuper
    override fun update() {
        job?.cancel()
        job = sourceFlow
            .onStart { emit(DataState.Loading) }
            .onEmpty { emit(DataState.Empty) }
            .onEach {
                mData.value = it
                onEach(it)
            }
            .catch { error ->
                if (error !is CancellationException) {
                    if (logger != null && error.message != null){
                        logger.log(error.message.orEmpty())
                    }
                    emit(translateError(error))
                    if (BuildConfig.DEBUG)
                        Log.e(
                            this@DataViewModel.LTAG,
                            "Error while collecting ViewModel data", error
                        )
                }
            }
            .launchIn(viewModelScope + Dispatchers.IO)
    }

    protected open fun onEach(dataState: DataState<T>){

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