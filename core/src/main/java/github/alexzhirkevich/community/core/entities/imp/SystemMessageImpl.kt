package github.alexzhirkevich.community.core.entities.imp

import github.alexzhirkevich.community.core.entities.interfaces.Entity
import github.alexzhirkevich.community.core.entities.interfaces.Sendable
import github.alexzhirkevich.community.core.entities.interfaces.SystemMessage

class SystemMessageImpl(
    id : String = "",
    chatId : String ="",
    time : Long = System.currentTimeMillis(),
    override var message: Int = SystemMessage.MESSAGE_INVALID,
    override var metainf: Map<String, String>? = null,
) : SendableImpl(id = id, type = Sendable.TYPE_SYSTEM, chatId = chatId,time = time),
    SystemMessage {

    override fun compareTo(other: Entity): Int {
        TODO("Not yet implemented")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SystemMessageImpl) return false
        if (!super.equals(other)) return false

        if (message != other.message) return false
        if (metainf != other.metainf) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + message
        result = 31 * result + metainf.hashCode()
        return result
    }

    override fun toString(): String {
        return "SystemInfo(message=$message, metainf=$metainf)"
    }
}