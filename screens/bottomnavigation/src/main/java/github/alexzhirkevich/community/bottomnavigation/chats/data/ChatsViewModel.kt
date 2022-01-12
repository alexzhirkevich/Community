package github.alexzhirkevich.community.bottomnavigation.chats.data

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import github.alexzhirkevich.community.common.ChatWrap
import github.alexzhirkevich.community.common.DataState
import github.alexzhirkevich.community.common.DataViewModel
import github.alexzhirkevich.community.common.composable.SelectionState
import github.alexzhirkevich.community.core.EntityNotFoundException
import github.alexzhirkevich.community.core.Response
import github.alexzhirkevich.community.core.di.Stage
import github.alexzhirkevich.community.core.entities.imp.UserImpl
import github.alexzhirkevich.community.core.entities.interfaces.Chat
import github.alexzhirkevich.community.core.entities.interfaces.Dialog
import github.alexzhirkevich.community.core.entities.interfaces.Group
import github.alexzhirkevich.community.core.logger.Logger
import github.alexzhirkevich.community.core.repo.interfaces.ChatsRepository
import github.alexzhirkevich.community.core.repo.interfaces.CurrentUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalStdlibApi
@HiltViewModel
class ChatsViewModel @Inject constructor(
    @Stage private val currentUserRepository: CurrentUserRepository,
    @Stage private val chatsRepository : ChatsRepository,
    logger : Logger
) : DataViewModel<List<ChatWrap>>(logger) {

    val selectionState = SelectionState()

    private val chatsFlow = chatsRepository.getAll(currentUserRepository.currentUserId)
        .onEach {
            it.filterIsInstance<Response.Error>().forEach { resp ->
                val err = (resp as? Response.Error)?.error
                if (err is EntityNotFoundException && err.collectionId != null) {
                    viewModelScope.launch(Dispatchers.IO) {
                        kotlin.runCatching {
                            chatsRepository.remove(err.id, err.collectionId!!)
                        }
                    }
                }
            }
        }
        .map {
            it.filterIsInstance<Response.Success<Chat>>()
        }.catch { t ->
            logger.log("Failed to get chat list: ${t.message}")
        }

    override val sourceFlow: Flow<DataState<List<ChatWrap>>> =
        chatsFlow.mapNotNull {
            if (it.any { resp -> resp.isFromCache || resp.hasPendingWrites })
                DataState.Cached(
                    it.mapNotNull {
                        when (val c = it.value) {
                            is Group ->
                                ChatWrap.GroupWrap(c, 0, true,-1)
                            is Dialog ->
                                ChatWrap.DialogWrap(c, 0, true,UserImpl())
                            else -> null
                        }
                    }, it.any { it.hasPendingWrites }
                )
            else DataState.Success(
                it.mapNotNull {
                    when (val c = it.value) {
                        is Group ->
                            ChatWrap.GroupWrap(c, 0, true,-1)
                        is Dialog ->
                            ChatWrap.DialogWrap(c, 0, true,UserImpl())
                        else -> null
                    }
                })
        }

    init {
        update()
    }
}