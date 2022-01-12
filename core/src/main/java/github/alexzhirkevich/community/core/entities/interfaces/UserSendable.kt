package github.alexzhirkevich.community.core.entities.interfaces

interface UserSendable<T : Sendable> : Sendable {
    var senderId : String
    var replyTo: T?
}