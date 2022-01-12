package github.alexzhirkevich.community.core.entities.imp

import github.alexzhirkevich.community.core.entities.MediaContent
import github.alexzhirkevich.community.core.entities.Voice
import github.alexzhirkevich.community.core.entities.interfaces.Entity
import github.alexzhirkevich.community.core.entities.interfaces.Post

class PostImpl(
    id: String="",
    chatId: String="",
    senderId: String="",
    time: Long = System.currentTimeMillis(),
    replyTo : Post?=null,
    override var text: String? = null,
    override var content: List<MediaContent>? = null,
    override var voice: Voice?  = null,
) : UserSendableImpl<Post>(
    id = id,
    chatId = chatId,
    replyTo = replyTo,
    senderId = senderId,
    time = time
), Post {

    override fun compareTo(other: Entity): Int =
            if (other is PostImpl) time.compareTo(other.time) else 0

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PostImpl) return false
        if (!super.equals(other)) return false

        if (text != other.text) return false
        if (content != other.content) return false
        if (voice != other.voice) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (text?.hashCode() ?: 0)
        result = 31 * result + (content?.hashCode() ?: 0)
        result = 31 * result + (voice?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "PostImpl(text=$text, content=$content, voice=$voice)"
    }

}