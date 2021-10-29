package github.alexzhirkevich.community.core.entities.interfaces

import github.alexzhirkevich.community.core.entities.Voice

sealed interface VoiceSendable  : Entity {
    var voice : Voice?
}