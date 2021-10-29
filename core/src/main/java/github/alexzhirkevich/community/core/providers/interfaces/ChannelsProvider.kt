package github.alexzhirkevich.community.core.providers.interfaces

import github.alexzhirkevich.community.core.entities.imp.Admin
import github.alexzhirkevich.community.core.entities.interfaces.User
import github.alexzhirkevich.community.core.entities.interfaces.Channel
import github.alexzhirkevich.community.core.Response
import github.alexzhirkevich.community.core.providers.base.*
import kotlinx.coroutines.flow.Flow

interface ChannelsProvider :
        EntityProvider<Channel>,
        DependentRangeEntityProvider<Channel>,
        DependentRemovable,
        LinkProvider, IndependentProvider {

    fun find(namePart: String,limit: Int = 30): Flow<Collection<Response<Channel>>>

    suspend fun join(channelId: String): Response<Channel>

    fun getAdmins(channelId: String) : Flow<Collection<Response<Admin>>>

    fun getSubscribersCount(channelId: String) : Flow<Response<Long>>

    fun findByTag(tag: String): Flow<Response<Channel>>
}