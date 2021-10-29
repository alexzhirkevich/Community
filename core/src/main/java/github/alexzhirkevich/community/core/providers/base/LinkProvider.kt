package github.alexzhirkevich.community.core.providers.base

interface LinkProvider : Provider{

    fun createInviteLink(id : String) : String
}