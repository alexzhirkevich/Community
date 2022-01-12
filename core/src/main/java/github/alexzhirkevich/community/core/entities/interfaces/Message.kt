package github.alexzhirkevich.community.core.entities.interfaces

interface Message :
    UserSendable<Message>,
    TextSendable<Message>,
    VoiceSendable {
    var isViewed : Boolean
}