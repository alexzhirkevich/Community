package github.alexzhirkevich.community.common.util

fun <T> T.listOf() = listOf(this)

val Any.LTAG : String get() = this::class.java.simpleName

class SingleAction  {
    var isEmitted : Boolean = false
        private set

    fun doSingle(action : Runnable) {
        if (!isEmitted) {
            isEmitted = true
            action.run()
        }
    }
}