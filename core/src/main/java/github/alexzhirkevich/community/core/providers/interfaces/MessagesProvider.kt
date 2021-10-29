package github.alexzhirkevich.community.core.providers.interfaces

import github.alexzhirkevich.community.core.entities.interfaces.Chat
import github.alexzhirkevich.community.core.entities.interfaces.Sendable
import github.alexzhirkevich.community.core.Response
import github.alexzhirkevich.community.core.providers.base.DependentEntityProvider
import github.alexzhirkevich.community.core.providers.base.DependentRangeEntityProvider
import github.alexzhirkevich.community.core.providers.base.DependentRemovable
import github.alexzhirkevich.community.core.providers.base.IndependentProvider
import kotlinx.coroutines.flow.Flow

interface MessagesProvider :
        DependentEntityProvider<Sendable, Chat>,
        DependentRemovable,
        DependentRangeEntityProvider<Sendable>, IndependentProvider {

    fun last(chatId: String) : Flow<Response<Sendable>>
}