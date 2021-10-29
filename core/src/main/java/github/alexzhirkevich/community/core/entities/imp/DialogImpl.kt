package github.alexzhirkevich.community.core.entities.imp

import github.alexzhirkevich.community.core.entities.interfaces.Chat
import github.alexzhirkevich.community.core.entities.interfaces.Dialog
import github.alexzhirkevich.community.core.entities.interfaces.Sendable

class DialogImpl(
        id : String="",
        override var user1: String="",
        override var user2: String="",
        lastMessage: Sendable?= null)
    : ChatImpl(type = Chat.TYPE_DIALOG, id= id,lastMessage = lastMessage), Dialog {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DialogImpl) return false
        if (!super.equals(other)) return false

        if (user1 != other.user1) return false
        if (user2 != other.user2) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + user1.hashCode()
        result = 31 * result + user2.hashCode()
        return result
    }

    override fun toString(): String {
        return "Dialog(user1='$user1', user2='$user2')"
    }
}