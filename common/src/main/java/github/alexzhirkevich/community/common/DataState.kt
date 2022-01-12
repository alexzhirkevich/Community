package github.alexzhirkevich.community.common

import androidx.annotation.StringRes
import github.alexzhirkevich.community.core.Response

sealed interface DataState<out T> {

    object Empty : DataState<Nothing>

    object Loading : DataState<Nothing>

    open class Success<out T>(val value : T) : DataState<T>

    class Cached<out T>(value : T, val hasPendingWrites: Boolean)
        : Success<T>(value)

    class Error(@StringRes val message : Int, val error : Throwable) : DataState<Nothing>
}

fun <T> DataState<T>.valueOrNull() = (this as? DataState.Success)?.value

fun <T> Response<T>.toDataState(
    parseError : (Throwable) -> (Int) = { R.string.error_load_data }
) : DataState<T> =
    toDataState(transform = {it}, parseError = parseError)

fun <T,P> Response<T>.toDataState(
    transform : (T) -> P,
    parseError : (Throwable) -> (Int) = { R.string.error_load_data }
) : DataState<P> =
    when(val res = this) {
        is Response.Success ->
            if (res.isFromCache || res.hasPendingWrites)
                DataState.Cached(transform(res.value), res.hasPendingWrites)
            else DataState.Success(transform(res.value))

        is Response.Error -> DataState.Error(parseError(res.error), error = res.error)
    }