package github.alexzhirkevich.community.core.repo.interfaces

import github.alexzhirkevich.community.core.entities.imp.Admin
import github.alexzhirkevich.community.core.entities.interfaces.Channel
import github.alexzhirkevich.community.core.Response
import github.alexzhirkevich.community.core.repo.base.*
import kotlinx.coroutines.flow.Flow

interface ChannelsRepository :
        EntityRepository<Channel>,
        DependentRangeEntityRepository<Channel>,
        DependentRemovable,
        LinkRepository, IndependentRepository {

    val postsRepository : PostsRepository

    fun find(namePart: String,limit: Int = 30): Flow<Collection<Response<Channel>>>

    suspend fun join(channelId: String): Response<Channel>

    fun getAdmins(channelId: String) : Flow<Collection<Response<Admin>>>

    fun getSubscribersCount(channelId: String) : Flow<Response<Long>>

    fun findByTag(tag: String): Flow<Response<Channel>>
}