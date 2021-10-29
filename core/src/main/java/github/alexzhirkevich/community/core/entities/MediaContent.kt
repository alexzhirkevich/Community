package github.alexzhirkevich.community.core.entities

import github.alexzhirkevich.community.core.entities.interfaces.Entity

data class MediaContent(var type: Int, var url: String ="" ) : Entity {

    override var id: String = url

    override fun compareTo(other: Entity): Int = 0

    companion object TYPE {
        const val IMAGE = 0
        const val VIDEO = 1
    }
}