package github.alexzhirkevich.community.core.entities.imp

import github.alexzhirkevich.community.core.entities.interfaces.Chat
import github.alexzhirkevich.community.core.entities.interfaces.Entity
import github.alexzhirkevich.community.core.entities.interfaces.Sendable

sealed class ChatImpl(
    final override var type: Int,
    id: String="",
    final override var lastMessage: Sendable?=null,
    final override var creationTime: Long = System.currentTimeMillis(),
) : EntityImpl(id), Chat {

    override fun compareTo(other: Entity): Int {
        if (other is ChatImpl) {
            if (lastMessage != null  && other.lastMessage != null)
                return lastMessage!!.compareTo(other.lastMessage!!)
            return other.creationTime.compareTo(other.creationTime)
        }
        return super.compareTo(other)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ChatImpl) return false

        if (type != other.type) return false
        if (lastMessage != other.lastMessage) return false
        if (creationTime != other.creationTime) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type
        result = 31 * result + (lastMessage?.hashCode() ?: 0)
        result = 31 * result + creationTime.hashCode()
        return result
    }

    override fun toString(): String {
        return "ChatImpl(type=$type, lastMessage=$lastMessage, creationTime=$creationTime)"
    }

}