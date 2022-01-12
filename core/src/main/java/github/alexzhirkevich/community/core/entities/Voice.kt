package github.alexzhirkevich.community.core.entities

import github.alexzhirkevich.community.core.entities.interfaces.Entity

data class Voice(override val url : String, val len: Long)
    : NetworkContent