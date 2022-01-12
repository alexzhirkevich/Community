package github.alexzhirkevich.community.core.repo.interfaces

import github.alexzhirkevich.community.core.entities.interfaces.Chat
import github.alexzhirkevich.community.core.repo.base.DependentRangeEntityRepository
import github.alexzhirkevich.community.core.repo.base.DependentRemovable
import github.alexzhirkevich.community.core.repo.base.EntityRepository
import github.alexzhirkevich.community.core.repo.base.IndependentRepository

interface ChatsRepository :
        EntityRepository<Chat>,
        DependentRangeEntityRepository<Chat>,
        DependentRemovable, IndependentRepository {

    val messagesRepository : MessagesRepository

    suspend fun invite(chatId: String,userId : String)
}