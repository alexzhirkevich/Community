package github.alexzhirkevich.community.screens.chat.data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import github.alexzhirkevich.community.audioplayer.AudioPlayer
import github.alexzhirkevich.community.common.*
import github.alexzhirkevich.community.core.di.Stage
import github.alexzhirkevich.community.core.entities.imp.Admin
import github.alexzhirkevich.community.core.entities.imp.UserImpl
import github.alexzhirkevich.community.core.entities.interfaces.Channel
import github.alexzhirkevich.community.core.entities.interfaces.Dialog
import github.alexzhirkevich.community.core.entities.interfaces.Group
import github.alexzhirkevich.community.core.repo.interfaces.ChatsRepository
import github.alexzhirkevich.community.core.repo.interfaces.StorageRepository
import github.alexzhirkevich.community.core.repo.interfaces.TaggableRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull

open class ChatDetailViewModel @AssistedInject constructor(
    @Assisted protected val id : String,
    @Assisted storageRepository: StorageRepository,
    @Assisted audioPlayer: AudioPlayer,
    @Stage protected val chatsRepository : ChatsRepository,
    @Stage taggableRepository: TaggableRepository,
    ) : DataViewModel<ChatWrap>(),
        TaggableRepository by taggableRepository {

    companion object {
        fun provideFactory(
            factory: Factory,
            id: String,
            audioPlayer: AudioPlayer,
            storageRepository: StorageRepository
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    return factory.create(id, audioPlayer, storageRepository) as T
                }
            }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            id: String,
            audioPlayer: AudioPlayer,
            storageRepository: StorageRepository
        ): ChatDetailViewModel
    }

    final override val sourceFlow: Flow<DataState<ChatWrap>> =
        chatsRepository.get(id).mapNotNull { resp ->
            kotlin.runCatching {

                resp.toDataState(
                    transform = {
                        when (it) {
                            is Group ->
                                ChatWrap.GroupWrap(it, 32, true, 1234)
                            is Dialog ->
                                ChatWrap.DialogWrap(
                                    it, 32, true,
                                    UserImpl(name = "Friend")
                                )
                            is Channel -> {
                                throw Exception("\"${javaClass.simpleName}: Got channel as a chat $it")
                            }
                            else -> {
                                throw Exception("\"${javaClass.simpleName}: Invalid chat")
                            }
                        }
                    }
                )
            }.onFailure {
                if (BuildConfig.DEBUG)
                    Log.e(javaClass.simpleName,"Failed to parse chat",it)
                it.message?.let { msg ->
                    logger?.log(msg)
                }
            }.getOrNull()
        }

    init {
        update()
    }
}