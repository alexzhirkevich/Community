package github.alexzhirkevich.community.core.providers.interfaces

import github.alexzhirkevich.community.core.providers.base.Provider

interface ChannelProfileProvider : Provider {

    suspend fun setName(name : String)

    suspend fun setTag(tag : String)

    suspend fun setDescription(text : String)

    suspend fun setImageUri(uri : String)
}