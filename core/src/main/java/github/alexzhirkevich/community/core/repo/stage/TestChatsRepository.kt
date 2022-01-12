package github.alexzhirkevich.community.core.repo.stage

import github.alexzhirkevich.community.core.Response
import github.alexzhirkevich.community.core.entities.imp.GroupImpl
import github.alexzhirkevich.community.core.entities.imp.MessageImpl
import github.alexzhirkevich.community.core.entities.interfaces.Chat
import github.alexzhirkevich.community.core.repo.interfaces.ChatsRepository
import github.alexzhirkevich.community.core.repo.interfaces.MessagesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class TestChatsRepository @Inject constructor(): ChatsRepository {

    override val messagesRepository: MessagesRepository =
        TestMessagesRepository()

    override fun getAll(collection: String, limit: Int): Flow<Collection<Response<Chat>>> {
        return flowOf(
            List(limit) {
                Response.Success(
                    GroupImpl(
                        id = it.toString(),
                        imageUri = if (it ==0)
                            "https://i.ytimg.com/vi/97gTzS9VFxo/hq720.jpg?sqp=-oaymwEXCNAFEJQDSFryq4qpAwkIARUAAIhCGAE=&rs=AOn4CLAb-iOj1IpkyoFhIWVwx5vsVeM2sA"
                        else "",
                        name = "Group $it",
                        lastMessage = MessageImpl(
                            text = "Last message $it"
                        )
                    ), false, false
                )
            }
        )
    }

    override suspend fun remove(id: String, collection: String) {
        TODO("Not yet implemented")
    }

    override fun get(id: String): Flow<Response<Chat>> {
       return flowOf(Response.Success(
           GroupImpl(
               id = id,
               name = "Group $id",
               imageUri = if (id == "0")
                   "https://i.ytimg.com/vi/97gTzS9VFxo/hq720.jpg?sqp=-oaymwEXCNAFEJQDSFryq4qpAwkIARUAAIhCGAE=&rs=AOn4CLAb-iOj1IpkyoFhIWVwx5vsVeM2sA"
                else "",
           ),
           isFromCache = false,
           hasPendingWrites = false
       ))
    }

    override suspend fun create(entity: Chat) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(entity: Chat) {
        TODO("Not yet implemented")
    }

    override suspend fun invite(chatId: String, userId: String) {
        TODO("Not yet implemented")
    }
}