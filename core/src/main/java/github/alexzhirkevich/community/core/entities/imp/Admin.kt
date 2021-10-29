package github.alexzhirkevich.community.core.entities.imp

import github.alexzhirkevich.community.core.entities.interfaces.Entity
import github.alexzhirkevich.community.core.entities.interfaces.IChannelAdmin

class Admin(
        id: String = "",
        override var canEdit: Boolean = false,
        override var canPost: Boolean = false,
        override var canDelete: Boolean = false,
        override var canBan: Boolean = false) : EntityImpl(id = id), IChannelAdmin {

    override fun compareTo(other: Entity): Int {
        if (other is Admin) {
            canBan.compareTo(other.canBan).let { if (it != 0) return it }
            canEdit.compareTo(other.canEdit).let { if (it != 0) return it }
            canDelete.compareTo(other.canDelete).let { if (it != 0) return it }
            return canPost.compareTo(other.canPost)
        }
        return super.compareTo(other)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Admin) return false

        if (canEdit != other.canEdit) return false
        if (canPost != other.canPost) return false
        if (canDelete != other.canDelete) return false
        if (canBan != other.canBan) return false

        return true
    }

    override fun hashCode(): Int {
        var result = canEdit.hashCode()
        result = 31 * result + canPost.hashCode()
        result = 31 * result + canDelete.hashCode()
        result = 31 * result + canBan.hashCode()
        return result
    }

    override fun toString(): String {
        return "ChannelAdmin(canEdit=$canEdit, canPost=$canPost, canDelete=$canDelete, canBan=$canBan)"
    }
}