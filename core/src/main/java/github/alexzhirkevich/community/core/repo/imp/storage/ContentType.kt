package github.alexzhirkevich.community.core.repo.imp.storage

import github.alexzhirkevich.community.core.entities.Document
import github.alexzhirkevich.community.core.entities.MediaContent
import github.alexzhirkevich.community.core.entities.NetworkContent

sealed class ContentType<T : NetworkContent>(val directory : String) {
    object Image : ContentType<MediaContent>("Images")
    object Video : ContentType<MediaContent>("Videos")
    object Gif : ContentType<MediaContent>("Gifs")
    object Doc : ContentType<Document>("Documents")
    object Voice : ContentType<github.alexzhirkevich.community.core.entities.Voice>("Voices")
    object Thumbnail : ContentType<NetworkContent>("Thumbnails")
    object Unknown : ContentType<NetworkContent>("Unknown")
}

