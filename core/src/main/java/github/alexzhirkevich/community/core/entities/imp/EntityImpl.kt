package github.alexzhirkevich.community.core.entities.imp

import github.alexzhirkevich.community.core.entities.interfaces.Entity

sealed class EntityImpl (final override var id: String = "") : Entity {
    override fun compareTo(other: Entity): Int = id.compareTo(other.id)
}