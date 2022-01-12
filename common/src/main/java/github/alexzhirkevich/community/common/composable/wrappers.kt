package github.alexzhirkevich.community.common

import github.alexzhirkevich.community.core.entities.imp.Admin
import github.alexzhirkevich.community.core.entities.interfaces.*

sealed interface EntityWrap

sealed interface ChatWrap : Chat, EntityWrap {

    val unreadCount: Int

    val isNotificationsEnabled : Boolean

    class GroupWrap(
        group: Group,
        override val unreadCount: Int,
        override val isNotificationsEnabled: Boolean,
        val membersCount: Long,
    ) : ChatWrap, Group by group

    class DialogWrap(
        dialog: Dialog,
        override val unreadCount: Int,
        override val isNotificationsEnabled: Boolean,
        val companion: User
    ) : ChatWrap, Dialog by dialog

    class ChannelWrap(
        channel: Channel,
        override val unreadCount: Int,
        val membersCount: Long,
        override val isNotificationsEnabled: Boolean,
        val isAdmin: Admin?
    ) : ChatWrap, Channel by channel
}

sealed interface SendableWrap : Sendable, EntityWrap {

    val hasPendingWrites : Boolean

    class PostWrap(
        post: Post,
        val channel: ChatWrap.ChannelWrap?,
        val viewsCount: Long,
        val sharesCount: Long,
        override val hasPendingWrites: Boolean
    ) : SendableWrap, Post by post

    class MessageWrap(
        message: Message,
        val chat: ChatWrap?,
        val sender : User,
        val isIncoming : Boolean,
        override val hasPendingWrites: Boolean
    ) : SendableWrap, Message by message

    class SystemMessageWrap(
        message: SystemMessage,
        val chat: ChatWrap?,
        override val hasPendingWrites: Boolean
    ) : SendableWrap, SystemMessage by message
}