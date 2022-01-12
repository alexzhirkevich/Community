package github.alexzhirkevich.community.core

sealed interface Loading<out T> {

    object None : Loading<Nothing>
    class Progress<T>(val value : Float, val totalBytes : Long, val intermediate : T? = null) : Loading<Nothing>
    class Error (val error : Throwable) : Loading<Nothing>
    class Success<out T> (val value :T) : Loading<T>
}

fun <T> Loading<T>?.orNone() : Loading<T> =
    this ?: Loading.None