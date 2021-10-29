package github.alexzhirkevich.community.core.sysmessages

import github.alexzhirkevich.community.core.entities.interfaces.SystemMessage

data class SmChannelCreated(val systemMessage: SystemMessage) : SystemMessage by systemMessage