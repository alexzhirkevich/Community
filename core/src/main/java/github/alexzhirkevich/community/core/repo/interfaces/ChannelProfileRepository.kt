package github.alexzhirkevich.community.core.repo.interfaces

import github.alexzhirkevich.community.core.repo.base.Repository

interface ChannelProfileRepository : Repository {

    suspend fun setName(id : String, name : String)

    suspend fun setTag(id : String, tag : String)

    suspend fun setDescription(id : String, text : String)

    suspend fun setImageUri(id : String, uri : String)
}