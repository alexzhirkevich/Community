package github.alexzhirkevich.community.core.entities.imp

import github.alexzhirkevich.community.core.entities.interfaces.Sendable
import github.alexzhirkevich.community.core.entities.interfaces.UserSendable

sealed class UserSendableImpl<T : Sendable>(
    id:String,
    chatId: String,
    time : Long,
    final override var senderId: String,
    override var replyTo: T? = null,
    ) : SendableImpl(id = id,type = Sendable.TYPE_USER, chatId = chatId,time = time),
    UserSendable<T> {

    override fun toString(): String {
        return "UserMessage(senderId='$senderId')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as UserSendableImpl<*>

        if (replyTo != other.replyTo) return false
        if (senderId != other.senderId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (replyTo?.hashCode() ?: 0)
        result = 31 * result + senderId.hashCode()
        return result
    }
}