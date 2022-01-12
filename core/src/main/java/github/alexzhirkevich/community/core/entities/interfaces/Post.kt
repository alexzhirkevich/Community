package github.alexzhirkevich.community.core.entities.interfaces

interface Post :
    UserSendable<Post>,
    TextSendable<Post>,
    VoiceSendable{
}