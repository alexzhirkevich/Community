package github.alexzhirkevich.community.core.repo.base

interface Synchronizable {

    val isSynchronized : Boolean

    fun addOnSynchronizationCompleteListener(action: Runnable)

    fun removeOnSynchronizationCompleteListener(action: Runnable)
}