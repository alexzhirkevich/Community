package github.alexzhirkevich.community.core.repo.data

data class PostData(
    override val id : String,
    val viewsCount : Long,
    val repostsCount : Long
) : EntityData