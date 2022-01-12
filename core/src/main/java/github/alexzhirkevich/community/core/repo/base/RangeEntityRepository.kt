package github.alexzhirkevich.community.core.repo.base

import github.alexzhirkevich.community.core.entities.interfaces.Entity
import github.alexzhirkevich.community.core.Response
import kotlinx.coroutines.flow.Flow

interface RangeEntityRepository<T: Entity> : Repository {

    fun getAll(limit:Int = 30) : Flow<Collection<Response<T>>>
}
