package github.alexzhirkevich.community.core.repo.base

interface Removable : Repository {

    suspend fun remove(id : String)
}