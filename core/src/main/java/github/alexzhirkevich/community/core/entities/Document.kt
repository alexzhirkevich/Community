package github.alexzhirkevich.community.core.entities

import androidx.annotation.IntRange

data class Document(
    @IntRange(from = TYPE_IMAGE, to = TYPE_AUDIO) val type : Long,
    override val url: String = "",
    val name : String = System.currentTimeMillis().toString(),
    val size : Long = 0,
) : NetworkContent {

    companion object {
        const val TYPE_UNKNOWN = 0L
        const val TYPE_IMAGE = 1L
        const val TYPE_VIDEO = 2L
        const val TYPE_AUDIO = 3L
    }
}