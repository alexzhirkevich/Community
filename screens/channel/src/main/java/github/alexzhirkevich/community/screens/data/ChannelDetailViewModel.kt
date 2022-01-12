package github.alexzhirkevich.community.screens.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import github.alexzhirkevich.community.audioplayer.AudioPlayer
import github.alexzhirkevich.community.common.*
import github.alexzhirkevich.community.core.di.Stage
import github.alexzhirkevich.community.core.entities.imp.Admin
import github.alexzhirkevich.community.core.repo.interfaces.ChannelsRepository
import github.alexzhirkevich.community.core.repo.interfaces.StorageRepository
import github.alexzhirkevich.community.core.repo.interfaces.TaggableRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

open class ChannelDetailViewModel @AssistedInject constructor(
    @Assisted id : String,
    @Assisted storageRepository: StorageRepository,
    @Assisted audioPlayer: AudioPlayer,
    @Stage private val channelRepository : ChannelsRepository,
    @Stage taggableRepository: TaggableRepository,
    ) : DataViewModel<ChatWrap.ChannelWrap>(),
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
                    return factory.create(id,audioPlayer, storageRepository) as T
                }
            }
    }
    @AssistedFactory
    interface Factory {
        fun create(
            id : String,
            audioPlayer: AudioPlayer,
            storageRepository: StorageRepository
        ) : ChannelDetailViewModel
    }

    final override val sourceFlow: Flow<DataState<ChatWrap.ChannelWrap>> =
        channelRepository.get(id).map { resp ->
            resp.toDataState(
                transform = {
                    ChatWrap.ChannelWrap(it, 32, 1234, true,Admin())
                }
            )
        }
    init {
        update()
    }
}