package github.alexzhirkevich.community.core.repo.base

interface DependentRemovable : Repository {

    suspend fun remove(id : String, collection: String)
}