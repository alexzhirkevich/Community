package github.alexzhirkevich.community.core.providers.base

interface Removable : Provider {

    suspend fun remove(id : String)
}