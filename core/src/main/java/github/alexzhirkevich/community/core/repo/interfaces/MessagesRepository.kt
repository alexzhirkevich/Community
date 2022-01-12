package github.alexzhirkevich.community.core.repo.interfaces

import github.alexzhirkevich.community.core.entities.interfaces.Chat
import github.alexzhirkevich.community.core.entities.interfaces.Sendable
import github.alexzhirkevich.community.core.Response
import github.alexzhirkevich.community.core.repo.base.DependentEntityRepository
import github.alexzhirkevich.community.core.repo.base.DependentRangeEntityRepository
import github.alexzhirkevich.community.core.repo.base.DependentRemovable
import github.alexzhirkevich.community.core.repo.base.IndependentRepository
import kotlinx.coroutines.flow.Flow

interface MessagesRepository :
        DependentEntityRepository<Sendable, Chat>,
        DependentRemovable,
        DependentRangeEntityRepository<Sendable>, IndependentRepository {

    fun last(chatId: String) : Flow<Response<Sendable>>
}