package github.alexzhirkevich.community.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import github.alexzhirkevich.community.core.Response
import github.alexzhirkevich.community.core.entities.imp.Admin
import github.alexzhirkevich.community.core.entities.interfaces.Post
import github.alexzhirkevich.community.core.entities.interfaces.Sendable
import github.alexzhirkevich.community.core.entities.interfaces.SystemMessage
import github.alexzhirkevich.community.core.providers.interfaces.ChannelsProvider
import github.alexzhirkevich.community.core.providers.interfaces.PostsProvider
import github.alexzhirkevich.community.data.ChatWrap
import github.alexzhirkevich.community.data.DataState
import github.alexzhirkevich.community.data.SendableWrap
import github.alexzhirkevich.community.data.test.TestChannelsProvider
import github.alexzhirkevich.community.data.test.TestPostsProvider
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*


class ChannelViewModel(private val id : String) : DataViewModel<ChatWrap.ChannelWrap>() {

    class Factory(private val id: String) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            ChannelViewModel(id) as T

    }

    private val channelProvider : ChannelsProvider by lazy {
        TestChannelsProvider()
    }

    private val postsProvider : PostsProvider by lazy {
        TestPostsProvider()
    }

    private val channelJob : Job?=null
    private val subJob : Job?= null
    private val adminJob : Job?= null

    override val sourceFlow: Flow<DataState<ChatWrap.ChannelWrap>> =
        channelProvider.get(id).map {
            when(it){
                is Response.Success ->
                    if (it.isFromCache || it.hasPendingWrites)
                        DataState.Cached(
                            ChatWrap.ChannelWrap(
                                it.value,13, 23, Admin()
                            ),it.hasPendingWrites
                        )
                    else DataState.Success(
                        ChatWrap.ChannelWrap(
                            it.value,13, 23, Admin()
                        )
                    )
                is Response.Error ->
                    DataState.Error(defaultErrorMessage,it.error)
            }
        }

    val posts: Flow<DataState<List<SendableWrap>>>
        = postsProvider.getAll(id).map { responses ->

        var hasPD = false
        var cached = false

        responses.filterIsInstance<Response.Success<Sendable>>()
            .mapNotNull {
                if (it.isFromCache || it.hasPendingWrites)
                    cached = true
                if (it.hasPendingWrites)
                    hasPD = true
                when (val msg = it.value) {
                    is Post -> SendableWrap.PostWrap(
                        post = msg,
                        121232133L,
                        213123L,
                        it.hasPendingWrites
                    )
                    is SystemMessage -> SendableWrap.SystemMessageWrap(
                        msg,
                        it.hasPendingWrites
                    )
                    else -> null
                }
            }.let { list ->
                if (cached) DataState.Cached(list, hasPD)
                else DataState.Success(list)
            }

    }.apply {
        launchIn(viewModelScope)
    }

    init {
        update()
    }
}

