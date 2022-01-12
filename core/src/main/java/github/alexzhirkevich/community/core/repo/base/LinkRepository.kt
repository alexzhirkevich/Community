package github.alexzhirkevich.community.core.repo.base

interface LinkRepository : Repository{

    fun createInviteLink(id : String) : String
}