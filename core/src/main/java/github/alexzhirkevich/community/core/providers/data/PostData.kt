package github.alexzhirkevich.community.core.providers.data

data class PostData(
    override val id : String,
    val viewsCount : Long,
    val repostsCount : Long
) : EntityData