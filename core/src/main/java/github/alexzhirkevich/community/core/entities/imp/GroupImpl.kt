package github.alexzhirkevich.community.core.entities.imp

import github.alexzhirkevich.community.core.entities.interfaces.Chat
import github.alexzhirkevich.community.core.entities.interfaces.Group
import github.alexzhirkevich.community.core.entities.interfaces.Sendable

class GroupImpl(
    id : String = "",
    override var name: String = "",
    override var imageUri: String = "",
    override var creatorId: String = "",
    creationTime: Long = 0,
    lastMessage: Sendable?=null, ) : ChatImpl(
        type = Chat.TYPE_GROUP,
        id = id,
        creationTime = creationTime,
        lastMessage = lastMessage), Group {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GroupImpl) return false
        if (!super.equals(other)) return false

        if (name != other.name) return false
        if (imageUri != other.imageUri) return false
        if (creatorId != other.creatorId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + imageUri.hashCode()
        result = 31 * result + creatorId.hashCode()
        return result
    }

    override fun toString(): String {
        return "GroupImpl(name='$name', imageUri='$imageUri', creatorId='$creatorId')"
    }
}