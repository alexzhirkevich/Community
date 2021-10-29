package github.alexzhirkevich.community.core.entities.interfaces

interface SystemMessage : Sendable {
    var message : Int
    var metainf : Map<String,String>?

    companion object {
        const val MESSAGE_INVALID = -1
        const val MESSAGE_CHANNEL_CREATED = 0
        const val MESSAGE_CHANNEL_AVATAR_CHANGED = 1
        const val MESSAGE_CHANNEL_NAME_CHANGED = 2

        const val META_URL = "url"
        const val META_BY = "by"
        const val META_WHO = "who"
        const val META_TO = "to"

        const val MESSAGE_CHAT_INVITED = 101
        const val MESSAGE_CHAT_AVATAR_CHANGED = 102
        const val MESSAGE_CHAT_NAME_CHANGED = 103

    }
}