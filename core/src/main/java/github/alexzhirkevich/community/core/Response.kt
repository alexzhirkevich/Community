package github.alexzhirkevich.community.core


sealed interface Response<out T>  {

    class Success<out T>(val value: T, val isFromCache : Boolean, val hasPendingWrites : Boolean)
        : Response<T>

    class Error(val error : Throwable) : Response<Nothing>

}


