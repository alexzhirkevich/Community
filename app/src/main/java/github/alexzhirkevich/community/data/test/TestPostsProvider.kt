package github.alexzhirkevich.community.data.test

import github.alexzhirkevich.community.core.Response
import github.alexzhirkevich.community.core.entities.imp.PostImpl
import github.alexzhirkevich.community.core.entities.imp.SystemMessageImpl
import github.alexzhirkevich.community.core.entities.interfaces.Channel
import github.alexzhirkevich.community.core.entities.interfaces.Sendable
import github.alexzhirkevich.community.core.entities.interfaces.SystemMessage
import github.alexzhirkevich.community.core.providers.data.PostData
import github.alexzhirkevich.community.core.providers.interfaces.PostsProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class TestPostsProvider : PostsProvider {

    override fun lastViewed(channelId: String): Flow<Response<String>> {
        TODO("Not yet implemented")
    }

    override fun getPostData(id: String, channelId: String): Flow<Response<PostData>> {
        TODO("Not yet implemented")
    }

    override fun get(id: String, collectionID: String): Flow<Response<Sendable>> {
        TODO("Not yet implemented")
    }

    override suspend fun create(entity: Sendable) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(entity: Sendable) {
        TODO("Not yet implemented")
    }

    override fun getAll(collection: String, limit: Int): Flow<Collection<Response<Sendable>>> {
        return flowOf(List(limit) {

            Response.Success(
                if (it !=0) PostImpl(
                    id = it.toString(),
                    chatId = collection,
                    time = System.currentTimeMillis() + it*6*60*60*1000,
                    text = "LK aksdfjh ksajdfk jhaskdjfhkajshf kdsh" +
                            "asdfjksdfjkhaskjdfhksajdfkhsadjfasjdfs" +
                            "sadfhf kasdjhf kjsah kjshdfk jahsdf asdkfj"
                ) else SystemMessageImpl(
                    id = it.toString(),
                    chatId = collection,
                    time = System.currentTimeMillis() + it*6*60*60*1000,
                    message = SystemMessage.MESSAGE_CHANNEL_CREATED
                )
                , false, false
            )
        })
    }

    override suspend fun remove(id: String, collection: String) {
        TODO("Not yet implemented")
    }
}