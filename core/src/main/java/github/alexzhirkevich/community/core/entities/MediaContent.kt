package github.alexzhirkevich.community.core.entities

data class MediaContent(
    var type: Int,
    val name : String = System.currentTimeMillis().toString(),
    override var url: String ="",
    var thumbnailUrl : String?=null,
    var width : Int?=null,
    var height : Int?=null,
    var size : Long = 0,
    ) : NetworkContent {

    companion object TYPE {
        const val IMAGE = 0
        const val VIDEO = 1
        const val GIF = 4
    }
}