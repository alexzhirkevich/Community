package github.alexzhirkevich.community.core.repo.stage

import github.alexzhirkevich.community.core.Response
import github.alexzhirkevich.community.core.entities.imp.PostImpl
import github.alexzhirkevich.community.core.entities.imp.SystemMessageImpl
import github.alexzhirkevich.community.core.entities.interfaces.Sendable
import github.alexzhirkevich.community.core.entities.interfaces.SystemMessage
import github.alexzhirkevich.community.core.repo.data.PostData
import github.alexzhirkevich.community.core.repo.interfaces.PostsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class TestPostsRepository @Inject constructor() : PostsRepository {

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
                    text = "LK **__boldunderline__** //--crosslineitalic--// vk.com jhaskdjfhkajshf __underline__ " +
                            "sfsd //italic//  sdfsdf sdfsd **bold** **s** sdfsdfsdfsdf sdfsdf --crossline-- " +
                            " //italic**bolditalic**// " + "**B B //BI BI __BIU BIU__ --BIC-- BI// B**" +
                            " --crossline-- kasdjhf @user **bold** kjshdfk @channel //italic// asdkfj"
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