package github.alexzhirkevich.community.screens.chat.data

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import github.alexzhirkevich.community.audioplayer.AudioPlayer
import github.alexzhirkevich.community.common.*
import github.alexzhirkevich.community.core.Response
import github.alexzhirkevich.community.core.di.Stage
import github.alexzhirkevich.community.core.entities.MessageBuilder
import github.alexzhirkevich.community.core.entities.imp.UserImpl
import github.alexzhirkevich.community.core.entities.interfaces.*
import github.alexzhirkevich.community.core.logger.Logger
import github.alexzhirkevich.community.core.repo.interfaces.*
import github.alexzhirkevich.community.features.aft.TagNavigator
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class ChatViewModel @AssistedInject constructor(
    @Assisted id : String,
    @Assisted storageRepository: StorageRepository,
    @Assisted audioPlayer: AudioPlayer,
    @Stage chatsRepository : ChatsRepository,
    @Stage private val usersRepository: UsersRepository,
    logger : Logger,
    @Stage private val taggableRepository: TaggableRepository,
    messageBuilder: MessageBuilder<Message>,
    ) : ChatDetailViewModel(id,storageRepository, audioPlayer,
        chatsRepository,taggableRepository),
    TaggableRepository by taggableRepository,
    AudioPlayer by audioPlayer,
    MessageBuilder<Message> by messageBuilder,
    StorageRepository by storageRepository {

    companion object {
        fun provideFactory(
            factory: Factory,
            id: String,
            audioPlayer: AudioPlayer,
            storageRepository: StorageRepository
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    return factory.create(id,storageRepository,audioPlayer,) as T
                }
            }
    }
    @AssistedFactory
    interface Factory {
        fun create(
            id: String,
            storageRepository: StorageRepository,
            audioPlayer: AudioPlayer,
            ) : ChatViewModel
    }

    fun sendMessage(message: Message, onSuccess : suspend () -> Unit) = viewModelScope
        .launch(Dispatchers.IO) {
            try {
                chatsRepository.messagesRepository.create(message)
                onSuccess()
            } catch (t: Throwable) {
                logger?.log("Failed to send message: ${t.message}")
            }
        }

    private val _firstNewMessage : MutableStateFlow<DataState<SendableWrap.MessageWrap>> =
        MutableStateFlow(DataState.Empty)

    val firstNewMessage: StateFlow<DataState<SendableWrap.MessageWrap>> =
        _firstNewMessage.asStateFlow()

//    override val sourceFlow: Flow<DataState<ChatWrap>> =
//        chatsRepository.get(id).map {
//            when(it){
//                is Response.Success ->
//                    when (val chat = it.value) {
//                        is Group ->
//                            if (it.isFromCache || it.hasPendingWrites)
//                                DataState.Cached(
//                                    ChatWrap.GroupWrap(
//                                        chat, 13, true,23,
//                                    ), it.hasPendingWrites
//                                )
//                            else DataState.Success(
//                                ChatWrap.GroupWrap(
//                                    chat, 13, true,23
//                                )
//                            )
//                        is Dialog ->
//                            if (it.isFromCache || it.hasPendingWrites)
//                                DataState.Cached(
//                                    ChatWrap.DialogWrap(
//                                        chat, 13, true,UserImpl(),
//                                    ), it.hasPendingWrites
//                                )
//                            else DataState.Success(
//                                ChatWrap.DialogWrap(
//                                    chat, 13,true, UserImpl()
//                                )
//                            )
//                        else -> DataState.Empty
//                    }
//                is Response.Error ->
//                    DataState.Error(defaultErrorMessage,it.error)
//            }
//        }
    private val mMessages = MutableStateFlow<DataState<List<SendableWrap>>>(DataState.Empty)

    val messages: StateFlow<DataState<List<SendableWrap>>> = mMessages.asStateFlow()

    private var messagesJob : Job?= null

    override fun translateError(throwable: Throwable): DataState.Error {
        return super.translateError(throwable)
    }
    override fun update() {
        super.update()

        messagesJob?.cancel()
        messagesJob = chatsRepository.messagesRepository
            .getAll(id).map { responses ->

                var hasPD = false
                var cached = false

                responses.filterIsInstance<Response.Success<Sendable>>()
                    .mapNotNull {
                        if (it.isFromCache || it.hasPendingWrites)
                            cached = true
                        if (it.hasPendingWrites)
                            hasPD = true
                        when (val msg = it.value) {
                            is Message -> SendableWrap.MessageWrap(
                                message = msg,
                                chat = data.value.valueOrNull(),
                                UserImpl(
                                    id = msg.senderId,
                                    name = "User $id"
                                ),
                                isIncoming = msg.senderId != usersRepository.currentUserId,
                                it.hasPendingWrites
                            )
                            is SystemMessage -> SendableWrap.SystemMessageWrap(
                                chat = data.value.valueOrNull(),
                                message = msg,
                                hasPendingWrites = it.hasPendingWrites
                            )
                            else -> null
                        }
                    }.let { list ->
                        if (cached) DataState.Cached(list, hasPD)
                        else DataState.Success(list)
                    }
            }.onEach {
                mMessages.tryEmit(it)

                val newMessage = it.value.find { msg ->
                    msg is SendableWrap.MessageWrap &&
                            msg.isIncoming &&
                            !msg.isViewed
                }?.let {
                    DataState.Success(it as SendableWrap.MessageWrap)
                } ?: DataState.Empty
                if (firstNewMessage.value is DataState.Empty ||
                        firstNewMessage.value is DataState.Success &&
                            newMessage is DataState.Success &&
                            (firstNewMessage.value as DataState.Success<SendableWrap.MessageWrap>)
                                .value.time > newMessage.value.time) {
                    _firstNewMessage.tryEmit(newMessage)
                }
            }
            .catch { t ->
                logger?.log("Failed to get messages from chat $id: ${t.message}")
            }
            .launchIn(viewModelScope + Dispatchers.IO)
    }

    init {
        update()
    }

    override fun onCleared() {
        super.onCleared()
        release()
    }

    override fun get(tag: String): Flow<Response<Taggable>> {
        return taggableRepository.get(tag)
    }


}

