package github.alexzhirkevich.community.core.entities.imp

import github.alexzhirkevich.community.core.entities.interfaces.Entity
import github.alexzhirkevich.community.core.entities.interfaces.Sendable

sealed class SendableImpl(
    id: String,
    final override var type: Int,
    final override var chatId: String,
    final override var time: Long = System.currentTimeMillis(),
) : EntityImpl(id = id), Sendable {

    override fun compareTo(other: Entity): Int {
        return if (other is Sendable){
            time.compareTo(other.time)
        } else super.compareTo(other)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SendableImpl) return false

        if (type != other.type) return false
        if (chatId != other.chatId) return false
        if (time != other.time) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type
        result = 31 * result + chatId.hashCode()
        result = 31 * result + time.hashCode()
        return result
    }

    override fun toString(): String {
        return "Message(type=$type, chatId='$chatId', time=$time)"
    }
}