package github.alexzhirkevich.community.data.viewmodels

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import github.alexzhirkevich.community.core.*
import github.alexzhirkevich.community.core.entities.imp.Admin
import github.alexzhirkevich.community.core.entities.interfaces.Channel
import github.alexzhirkevich.community.core.providers.interfaces.ChannelsProvider
import github.alexzhirkevich.community.core.providers.interfaces.CurrentUserProvider
import github.alexzhirkevich.community.data.ChatWrap
import github.alexzhirkevich.community.data.DataState
import github.alexzhirkevich.community.data.test.TestChannelsProvider
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.reflect.KClass

@FlowPreview
@ExperimentalCoroutinesApi
@ExperimentalStdlibApi
class ChannelsListViewModel : DataViewModel<List<ChatWrap.ChannelWrap>>() {

    private val channelsProvider: ChannelsProvider = TestChannelsProvider()

    private val currentUserProvider: CurrentUserProvider = object : CurrentUserProvider {
        override val currentUserId: String
            get() = "qwe"
    }

    private val currentIds: MutableStateFlow<List<String>> =
        MutableStateFlow(listOf("qwe","qwe"))

    private val channelsFlow = channelsProvider
        .getAll(currentUserProvider.currentUserId)
        .onEach {
            it.filterIsInstance<Response.Error>().forEach { resp ->
                val err = (resp as? Response.Error)?.error
                if (err is EntityNotFoundException && err.collectionId != null) {
                    viewModelScope.launch(Dispatchers.IO) {
                        kotlin.runCatching {
                            channelsProvider.remove(err.id, err.collectionId!!)
                        }
                    }
                }
            }
        }
        .map {
            it.filterIsInstance<Response.Success<Channel>>()
        }
        .onEach {
            currentIds.value = it.map { it.value.id }
        }
        .catch { err ->
            emit(emptyList())
            if (BuildConfig.DEBUG)
                Log.w(this@ChannelsListViewModel.LTAG, "Failed to get channels", err)
        }

    private val subsFlow = currentIds
        .flatMapLatest { ids ->
            ids.map { id ->
                id to channelsProvider.getSubscribersCount(id)
                    .filterIsInstance<Response.Success<Long>>()
                    .map { it.value }
            }.toMap().combine()
        }.catch { err ->
            if (BuildConfig.DEBUG)
                Log.w(this@ChannelsListViewModel.LTAG, "Failed to get subs", err)
        }

    private val adminsFlow = currentIds
        .flatMapLatest { ids ->
            ids.map { id ->
                id to channelsProvider.getAdmins(id)
                    .map {
                        it.filterIsInstance<Response.Success<Admin>>()
                            .map { it.value }
                    }
            }.toMap().combine()
        }.catch { err ->
            if (BuildConfig.DEBUG)
                Log.w(this@ChannelsListViewModel.LTAG, "Failed to get channels", err)
        }

    override val sourceFlow: Flow<DataState<List<ChatWrap.ChannelWrap>>> =
        channelsFlow.map {
            if (it.any { resp -> resp.isFromCache || resp.hasPendingWrites })
                DataState.Cached(
                    it.map {
                        ChatWrap.ChannelWrap(it.value, 0, -1, Admin())
                    }, it.any { it.hasPendingWrites }
                )
            else DataState.Success(
                it.map {
                    ChatWrap.ChannelWrap(it.value, 0, -1, Admin())
                })
        }
//        combine(channelsFlow, subsFlow, adminsFlow) { channels, subs, admins ->
//            setSynchronized(true)
//            if (channels.isNotEmpty()) {
//                DataState.Success(
//                    channels.map { channel ->
//                        ChatWrap.ChannelWrap(
//                            channel = channel,
//                            membersCount = subs[channel.id] ?: -1L,
//                            admins = admins[channel.id] ?: emptyList()
//                        )
//                    }
//                )
//
//            } else DataState.Empty
//        }.onEmpty {
//            emit(DataState.Empty)
//        }.onStart {
//            emit(DataState.Loading)
//        }.onCompletion {
//            emit(DataState.Loading)
//        }

    init {
        update()
    }
}