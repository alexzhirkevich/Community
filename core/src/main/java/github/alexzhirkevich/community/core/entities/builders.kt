package github.alexzhirkevich.community.core.entities

import github.alexzhirkevich.community.core.entities.imp.MessageImpl
import github.alexzhirkevich.community.core.entities.imp.PostImpl
import github.alexzhirkevich.community.core.entities.interfaces.Message
import github.alexzhirkevich.community.core.entities.interfaces.Post
import github.alexzhirkevich.community.core.entities.MediaContent as MediaContent

interface Builder<T>{
    fun build() : T
}

interface SendableBuilder {

    var text: String?
    var content : List<MediaContent>?
    var voice : Voice?

    fun setText(text : String?) : SendableBuilder

    fun setContent(content: List<MediaContent>?) : SendableBuilder

    fun setVoice(voice : Voice?) : SendableBuilder

}

class PostBuilder(chatId: String) : SendableBuilder, Builder<Post> {

    private val post = PostImpl(chatId = chatId)

    override var text: String?
        get() = post.text
        set(value) { setText(value) }

    override var content: List<MediaContent>?
        get() = post.content
        set(value) { setContent(value) }

    override var voice: Voice?
        get() = post.voice
        set(value) { setVoice(value) }

    override fun setText(text : String?) : PostBuilder {
        post.text = text
        return this
    }

    override fun setContent(content: List<MediaContent>?) : PostBuilder{
        post.content = content
        return this
    }

    override fun setVoice(voice : Voice?) : PostBuilder {
        post.voice = voice
        return this
    }

    override fun build() : Post = post
}

class MessageBuilder(channelId : String) : SendableBuilder, Builder<Message> {

    private val message = MessageImpl(chatId = channelId)

    override var text: String?
        get() = message.text
        set(value) { setText(value) }

    override var content: List<MediaContent>?
        get() = message.content
        set(value) { setContent(value) }

    override var voice: Voice?
        get() = message.voice
        set(value) { setVoice(value) }

    override fun setText(text : String?) : MessageBuilder {
        message.text = text
        return this
    }

    override fun setContent(content: List<MediaContent>?) : MessageBuilder{
        message.content = content
        return this
    }

    override fun setVoice(voice : Voice?) : MessageBuilder {
        message.voice = voice
        return this
    }

    fun setReplyTo(message : Message?) : MessageBuilder {
        this.message.replyTo = message
        return this
    }

    override fun build(): Message = message


}



