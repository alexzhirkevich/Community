package github.alexzhirkevich.community.core.entities

import github.alexzhirkevich.community.core.entities.imp.MessageImpl
import github.alexzhirkevich.community.core.entities.imp.PostImpl
import github.alexzhirkevich.community.core.entities.interfaces.Message
import github.alexzhirkevich.community.core.entities.interfaces.Post
import github.alexzhirkevich.community.core.entities.interfaces.Sendable
import javax.inject.Inject
import github.alexzhirkevich.community.core.entities.MediaContent as MediaContent

interface Builder<T>{
    fun build() : T
}

interface MessageBuilder<T : Sendable> {

    fun setReplyTo(message: T?) : MessageBuilder<T>

    fun setText(text: String?) : MessageBuilder<T>

    fun setVoice(voice: Voice?) : MessageBuilder<T>

    fun setContent(content: List<MediaContent>?): MessageBuilder<T>

    fun build(): T
}


class PostBuilderImpl @Inject constructor(): MessageBuilder<Post>, Builder<Post> {

    private var post = PostImpl()

    override fun setReplyTo(message: Post?): MessageBuilder<Post> {
        post.replyTo = message
        return this
    }

    override fun setText(text : String?) : PostBuilderImpl {
        post.text = text
        return this
    }

    override fun setContent(content: List<MediaContent>?) : PostBuilderImpl{
        post.content = content
        return this
    }

    override fun setVoice(voice : Voice?) : PostBuilderImpl {
        post.voice = voice
        return this
    }

    override fun build() : Post = post.also {
        post = PostImpl()
    }

}

class MessageBuilderImpl @Inject constructor() : MessageBuilder<Message> {

    private var message = MessageImpl()

    override fun setText(text : String?) : MessageBuilder<Message> {
        message.text = text
        return this
    }

    override fun setContent(content: List<MediaContent>?) : MessageBuilder<Message> {
        message.content = content
        return this
    }

    override fun setVoice(voice : Voice?) : MessageBuilder<Message>  {
        message.voice = voice
        return this
    }

    override fun setReplyTo(message : Message?) : MessageBuilder<Message>  {
        this.message.replyTo = message
        return this
    }

    override fun build(): Message = message.apply {
        message = MessageImpl()
    }


}



