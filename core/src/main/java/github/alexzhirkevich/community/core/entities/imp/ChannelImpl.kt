package github.alexzhirkevich.community.core.entities.imp

import github.alexzhirkevich.community.core.entities.interfaces.Channel
import github.alexzhirkevich.community.core.entities.interfaces.Chat
import github.alexzhirkevich.community.core.entities.interfaces.Sendable


class ChannelImpl(
    id : String = "",
    creationTime: Long = System.currentTimeMillis(),
    override var name : String= "",
    override var tag: String="",
    override var description: String="",
    override var imageUri: String = "",
    override var creatorId: String = "",
    lastPost: Sendable?=null) : ChatImpl(
        type = Chat.TYPE_CHANNEL,
        id = id, lastMessage = lastPost,
        creationTime = creationTime), Channel {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ChannelImpl) return false
        if (!super.equals(other)) return false

        if (tag != other.tag) return false
        if (description != other.description) return false
        if (imageUri != other.imageUri) return false
        if (creatorId != other.creatorId) return false
        if (creationTime != other.creationTime) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + tag.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + imageUri.hashCode()
        result = 31 * result + creatorId.hashCode()
        result = 31 * result + creationTime.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }

    override fun toString(): String {
        return "ChannelImpl(name='$name', tag='$tag', description='$description', " +
                "imageUri='$imageUri', creatorId='$creatorId', creationTime=$creationTime)"
    }

}