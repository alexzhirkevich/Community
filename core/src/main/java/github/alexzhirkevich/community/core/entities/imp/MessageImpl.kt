package github.alexzhirkevich.community.core.entities.imp

import github.alexzhirkevich.community.core.entities.MediaContent
import github.alexzhirkevich.community.core.entities.Voice
import github.alexzhirkevich.community.core.entities.interfaces.Message

open class MessageImpl(
    id : String = "",
    chatId: String = "",
    senderId : String = "",
    time : Long = System.currentTimeMillis(),
    override var text: String? = null,
    override var content: List<MediaContent>?= null,
    override var voice: Voice? = null,
    final override var replyTo: Message? = null,
    final override var isViewed: Boolean = false,

    ) : UserSendableImpl<Message>(id = id, chatId = chatId,senderId = senderId,time = time),
    Message {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MessageImpl) return false
        if (!super.equals(other)) return false

        if (text != other.text) return false
        if (content != other.content) return false
        if (voice != other.voice) return false
        if (replyTo != other.replyTo) return false
        if (isViewed != other.isViewed) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (text?.hashCode() ?: 0)
        result = 31 * result + (content?.hashCode() ?: 0)
        result = 31 * result + (voice?.hashCode() ?: 0)
        result = 31 * result + (replyTo?.hashCode() ?: 0)
        result = 31 * result + isViewed.hashCode()
        return result
    }

    override fun toString(): String {
        return "MessageImpl(text=$text, content=$content, voice=$voice, replyTo=$replyTo, isViewed=$isViewed)"
    }
}