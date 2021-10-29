package github.alexzhirkevich.community.core.providers.interfaces

import github.alexzhirkevich.community.core.entities.interfaces.Channel
import github.alexzhirkevich.community.core.entities.interfaces.Sendable
import github.alexzhirkevich.community.core.Response
import github.alexzhirkevich.community.core.providers.base.DependentEntityProvider
import github.alexzhirkevich.community.core.providers.base.DependentRangeEntityProvider
import github.alexzhirkevich.community.core.providers.base.DependentRemovable
import github.alexzhirkevich.community.core.providers.base.IndependentProvider
import github.alexzhirkevich.community.core.providers.data.PostData
import kotlinx.coroutines.flow.Flow

interface PostsProvider :
        DependentEntityProvider<Sendable, Channel>,
        DependentRangeEntityProvider<Sendable>,
        DependentRemovable, IndependentProvider {
    fun lastViewed(channelId: String) : Flow<Response<String>>

    fun getPostData(id : String, channelId: String) : Flow<Response<PostData>>
}