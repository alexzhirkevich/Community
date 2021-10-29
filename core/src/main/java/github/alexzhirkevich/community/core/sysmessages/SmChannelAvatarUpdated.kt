package github.alexzhirkevich.community.core.sysmessages

import github.alexzhirkevich.community.core.entities.interfaces.SystemMessage

data class SmChannelAvatarUpdated(val systemMessage: SystemMessage) : SystemMessage by systemMessage {
    val url : String? = metainf?.get("url")?.toString()
}