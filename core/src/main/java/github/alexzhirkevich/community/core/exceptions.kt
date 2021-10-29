package github.alexzhirkevich.community.core

import github.alexzhirkevich.community.core.entities.interfaces.Entity

sealed class CoreException @JvmOverloads constructor(msg : String?=null, cause: Throwable?=null)
    : Exception(msg.orEmpty(),cause)

class EntityNotFoundException(
        clazz: Class<out Entity>,
        val id : String,
        val collectionId: String? = null
    ) : SnapshotNotFoundException(
    clazz = clazz,
    id = id,
    collectionId = collectionId
)

open class SnapshotNotFoundException @JvmOverloads constructor(msg: String?, cause : Throwable?=null)
    : CoreException(msg,cause) {
    constructor(
        clazz: Class<*>? = null,
        id: String? = null,
        collectionId: String? = null
    ) : this(buildString {
        append(clazz?.simpleName ?: "Snapshot")
        if (id != null) {
            append(" with id $id")
        }
        if (collectionId != null) {
            append(" in collection $collectionId")
        }
        append("not found")
    })
}