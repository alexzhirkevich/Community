package github.alexzhirkevich.community.data

import androidx.annotation.StringRes

sealed interface DataState<out T> {

    object Empty : DataState<Nothing>

    object Loading : DataState<Nothing>


    open class Success<out T>(val value : T) : DataState<T>

    class Cached<out T>(value : T, val hasPendingWrites: Boolean)
        : Success<T>(value)

    class Error(@StringRes val message : Int, val error : Throwable) : DataState<Nothing>
}