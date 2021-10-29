package github.alexzhirkevich.community.core.providers.base

import github.alexzhirkevich.community.core.entities.interfaces.Entity
import github.alexzhirkevich.community.core.Response
import kotlinx.coroutines.flow.Flow

interface EntityProvider<T : Entity> : Provider {

    fun get(id : String) : Flow<Response<T>>

    suspend fun create(entity : T)

    suspend fun delete(entity : T)
}