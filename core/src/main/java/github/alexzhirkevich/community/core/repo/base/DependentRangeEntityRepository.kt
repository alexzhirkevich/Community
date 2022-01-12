package github.alexzhirkevich.community.core.repo.base

import github.alexzhirkevich.community.core.entities.interfaces.Entity
import github.alexzhirkevich.community.core.Response
import kotlinx.coroutines.flow.Flow

interface DependentRangeEntityRepository<T: Entity> : Repository {
    /**
     * Creates observable for last [limit] entities in [collection].
     * If [limit] is -1, requests all entities in collection.
     * [limit] is 30 by default
     * * @return [Observable] state of requested model
     * */
    fun getAll(collection: String, limit:Int = 30) : Flow<Collection<Response<T>>>
}