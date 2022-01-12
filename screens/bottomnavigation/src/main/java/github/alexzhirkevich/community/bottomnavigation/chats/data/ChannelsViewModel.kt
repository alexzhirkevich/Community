package github.alexzhirkevich.community.bottomnavigation.chats.data

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import github.alexzhirkevich.community.common.ChatWrap
import github.alexzhirkevich.community.common.DataState
import github.alexzhirkevich.community.common.DataViewModel
import github.alexzhirkevich.community.common.composable.SelectionState
import github.alexzhirkevich.community.common.util.LTAG
import github.alexzhirkevich.community.core.*
import github.alexzhirkevich.community.core.di.Stage
import github.alexzhirkevich.community.core.entities.imp.Admin
import github.alexzhirkevich.community.core.entities.interfaces.Channel
import github.alexzhirkevich.community.core.logger.Logger
import github.alexzhirkevich.community.core.repo.interfaces.ChannelsRepository
import github.alexzhirkevich.community.core.repo.interfaces.CurrentUserRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@ExperimentalStdlibApi
@HiltViewModel
class ChannelsViewModel @Inject constructor(
    @Stage private val currentUserRepository: CurrentUserRepository,
    @Stage private val channelsRepository: ChannelsRepository,
    logger : Logger
) : DataViewModel<List<ChatWrap.ChannelWrap>>(logger) {

    val selectionState = SelectionState()

    private val currentIds: MutableStateFlow<List<String>> =
        MutableStateFlow(listOf("qwe","qwe"))

    private val channelsFlow = channelsRepository
        .getAll(currentUserRepository.currentUserId)
        .onEach {
            it.filterIsInstance<Response.Error>().forEach { resp ->
                val err = (resp as? Response.Error)?.error
                if (err is EntityNotFoundException && err.collectionId != null) {
                    viewModelScope.launch(Dispatchers.IO) {
                        kotlin.runCatching {
                            channelsRepository.remove(err.id, err.collectionId!!)
                        }
                    }
                }
            }
        }
        .map {
            it.filterIsInstance<Response.Success<Channel>>()
        }
        .onEach {
            currentIds.value = it.map {
                it.value.id
            }
        }
        .catch { err ->
            emit(emptyList())
            if (BuildConfig.DEBUG)
                Log.w(this@ChannelsViewModel.LTAG, "Failed to get channels", err)
        }

    private val subsFlow = currentIds
        .flatMapLatest { ids ->
            ids.map { id ->
                id to channelsRepository.getSubscribersCount(id)
                    .filterIsInstance<Response.Success<Long>>()
                    .map { it.value }
            }.toMap().combine()
        }.catch { err ->
            if (BuildConfig.DEBUG)
                Log.w(this@ChannelsViewModel.LTAG, "Failed to get subs", err)
        }

    private val adminsFlow = currentIds
        .flatMapLatest { ids ->
            ids.map { id ->
                id to channelsRepository.getAdmins(id)
                    .map {
                        it.filterIsInstance<Response.Success<Admin>>()
                            .map { it.value }
                    }
            }.toMap().combine()
        }.catch { err ->
            if (BuildConfig.DEBUG)
                Log.w(this@ChannelsViewModel.LTAG, "Failed to get channels", err)
        }

    override val sourceFlow: Flow<DataState<List<ChatWrap.ChannelWrap>>> =
        channelsFlow.map {
            if (it.any { resp -> resp.isFromCache || resp.hasPendingWrites })
                DataState.Cached(
                    it.map {
                        ChatWrap.ChannelWrap(it.value, 0 ,-1, true,Admin())
                    }, it.any { it.hasPendingWrites }
                )
            else DataState.Success(
                it.map {
                    ChatWrap.ChannelWrap(it.value, 0, -1, true,Admin())
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