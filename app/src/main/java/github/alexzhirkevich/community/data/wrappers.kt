package github.alexzhirkevich.community.data

import github.alexzhirkevich.community.core.entities.imp.Admin
import github.alexzhirkevich.community.core.entities.interfaces.*

sealed interface EntityWrap

sealed interface ChatWrap : Chat, EntityWrap {

    val unreadCount: Int

    class GroupWrap(
        group: Group,
        override val unreadCount: Int,
        val membersCount: Long
    ) : ChatWrap, Group by group

    class DialogWrap(
        dialog: Dialog,
        override val unreadCount: Int,
        val companion: User
    ) : ChatWrap, Dialog by dialog

    class ChannelWrap(
        channel: Channel,
        override val unreadCount: Int,
        val membersCount: Long,
        val isAdmin: Admin?
    ) : ChatWrap, Channel by channel
}

sealed interface SendableWrap : Sendable, EntityWrap {

    val hasPendingWrites : Boolean

    class PostWrap(
        post: Post,
        val viewsCount: Long,
        val sharesCount: Long,
        override val hasPendingWrites: Boolean
    ) : SendableWrap, Post by post

    class MessageWrap(
        message: Message,
        override val hasPendingWrites: Boolean
    ) : SendableWrap, Message by message

    class SystemMessageWrap(
        message: SystemMessage,
        override val hasPendingWrites: Boolean
    ) : SendableWrap, SystemMessage by message
}