package github.alexzhirkevich.community.core.providers.base

import github.alexzhirkevich.community.core.entities.interfaces.Entity

interface DependentRemovable : Provider {

    suspend fun remove(id : String, collection: String)
}