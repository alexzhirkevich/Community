package github.alexzhirkevich.community.core.providers.interfaces

import github.alexzhirkevich.community.core.entities.interfaces.Chat
import github.alexzhirkevich.community.core.entities.interfaces.User
import github.alexzhirkevich.community.core.providers.base.DependentRangeEntityProvider
import github.alexzhirkevich.community.core.providers.base.DependentRemovable
import github.alexzhirkevich.community.core.providers.base.EntityProvider
import github.alexzhirkevich.community.core.providers.base.IndependentProvider

interface ChatsProvider :
        EntityProvider<Chat>,
        DependentRangeEntityProvider<Chat>,
        DependentRemovable, IndependentProvider {

    suspend fun invite(chatId: String,userId : String)
}