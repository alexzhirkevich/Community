package github.alexzhirkevich.community.core

import java.io.IOException

sealed interface Loading<out T> {

    class Progress(val value : Float) : Loading<Nothing>
    class Error(val error : Throwable) : Loading<Nothing>
    class Success<out T>(val value :T) : Loading<T>
}


class LoadingException @JvmOverloads constructor(msg : String? = null,cause : Throwable?=null)
    : CoreException(msg, cause)