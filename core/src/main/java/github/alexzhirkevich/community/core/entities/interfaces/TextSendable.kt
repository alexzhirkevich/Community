package github.alexzhirkevich.community.core.entities.interfaces

import github.alexzhirkevich.community.core.entities.MediaContent

interface TextSendable  : UserSendable {
    var text: String?
    var content: List<MediaContent>?
}