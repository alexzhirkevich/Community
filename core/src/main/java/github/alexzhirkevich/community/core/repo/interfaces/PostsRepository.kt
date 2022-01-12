package github.alexzhirkevich.community.core.repo.interfaces

import github.alexzhirkevich.community.core.entities.interfaces.Channel
import github.alexzhirkevich.community.core.entities.interfaces.Sendable
import github.alexzhirkevich.community.core.Response
import github.alexzhirkevich.community.core.repo.base.DependentEntityRepository
import github.alexzhirkevich.community.core.repo.base.DependentRangeEntityRepository
import github.alexzhirkevich.community.core.repo.base.DependentRemovable
import github.alexzhirkevich.community.core.repo.base.IndependentRepository
import github.alexzhirkevich.community.core.repo.data.PostData
import kotlinx.coroutines.flow.Flow

interface PostsRepository :
        DependentEntityRepository<Sendable, Channel>,
        DependentRangeEntityRepository<Sendable>,
        DependentRemovable, IndependentRepository {
    //fun lastViewed(channelId: String) : Flow<Response<String>>

    fun getPostData(id : String, channelId: String) : Flow<Response<PostData>>
}