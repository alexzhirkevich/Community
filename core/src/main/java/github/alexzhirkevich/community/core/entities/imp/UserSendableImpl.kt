package github.alexzhirkevich.community.core.entities.imp

import github.alexzhirkevich.community.core.entities.interfaces.Sendable
import github.alexzhirkevich.community.core.entities.interfaces.UserSendable

sealed class UserSendableImpl(
    id:String,
    chatId: String,
    time : Long,
    final override var senderId: String,
) : SendableImpl(id = id,type = Sendable.TYPE_USER, chatId = chatId,time = time),
    UserSendable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UserSendableImpl) return false

        if (senderId != other.senderId) return false

        return true
    }

    override fun hashCode(): Int {
        return senderId.hashCode()
    }

    override fun toString(): String {
        return "UserMessage(senderId='$senderId')"
    }
}