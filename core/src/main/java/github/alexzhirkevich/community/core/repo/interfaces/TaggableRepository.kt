package github.alexzhirkevich.community.core.repo.interfaces

import github.alexzhirkevich.community.core.Response
import github.alexzhirkevich.community.core.entities.interfaces.Taggable
import github.alexzhirkevich.community.core.repo.base.Repository
import kotlinx.coroutines.flow.*

interface TaggableRepository : Repository {
    fun get(tag : String) : Flow<Response<Taggable>>
}

