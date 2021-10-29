package github.alexzhirkevich.community.core.entities.interfaces

interface Message : UserSendable, TextSendable, VoiceSendable {
    var replyTo : Message?
    var isViewed : Boolean
}