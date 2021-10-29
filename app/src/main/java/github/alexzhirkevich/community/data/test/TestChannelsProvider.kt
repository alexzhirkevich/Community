package github.alexzhirkevich.community.data.test

import github.alexzhirkevich.community.core.entities.imp.Admin
import github.alexzhirkevich.community.core.entities.interfaces.Channel
import github.alexzhirkevich.community.core.entities.interfaces.User
import github.alexzhirkevich.community.core.Response
import github.alexzhirkevich.community.core.entities.imp.ChannelImpl
import github.alexzhirkevich.community.core.providers.interfaces.ChannelsProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class TestChannelsProvider : ChannelsProvider {
    override fun find(namePart: String, limit: Int): Flow<Collection<Response<Channel>>> {
        TODO("Not yet implemented")
    }

    override suspend fun join(channelId: String): Response<Channel> {
        TODO("Not yet implemented")
    }

    override fun getAdmins(channelId: String): Flow<Collection<Response<Admin>>> =
        flowOf(emptyList())


    override fun getSubscribersCount(channelId: String): Flow<Response<Long>> =
        flowOf(Response.Success(
            value = 132L,
            isFromCache = true,
            hasPendingWrites = false
        ))

    override fun findByTag(tag: String): Flow<Response<Channel>> {
        TODO("Not yet implemented")
    }

    override fun get(id: String): Flow<Response<Channel>> {
        return flowOf(Response.Success(
            ChannelImpl(id = id,name = "Channel"),
            isFromCache = false,
            hasPendingWrites = false
        ))
    }

    override suspend fun create(entity: Channel) {
    }

    override suspend fun delete(entity: Channel) {
    }

    override fun getAll(collection: String, limit: Int): Flow<Collection<Response<Channel>>> =
        flowOf(List(limit) {
                Response.Success(
                    value = ChannelImpl(id = it.toString(), name = "Channel $it"),
                    isFromCache = false,
                    hasPendingWrites = false
                )
            })



    override suspend fun remove(id: String, collection: String) {
    }

    override fun createInviteLink(id: String): String {
        TODO("Not yet implemented")
    }
}