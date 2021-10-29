package github.alexzhirkevich.community.core.providers.base

import github.alexzhirkevich.community.core.entities.interfaces.Entity
import github.alexzhirkevich.community.core.Response
import kotlinx.coroutines.flow.Flow

interface RangeEntityProvider<T: Entity> : Provider {

    fun getAll(limit:Int = 30) : Flow<Collection<Response<T>>>
}
