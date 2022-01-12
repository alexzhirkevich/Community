package github.alexzhirkevich.community.screens.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import github.alexzhirkevich.community.audioplayer.AudioPlayer
import github.alexzhirkevich.community.common.*
import github.alexzhirkevich.community.core.repo.interfaces.TaggableRepository
import github.alexzhirkevich.community.core.Response
import github.alexzhirkevich.community.core.di.Stage
import github.alexzhirkevich.community.core.entities.MessageBuilder
import github.alexzhirkevich.community.core.entities.interfaces.*
import github.alexzhirkevich.community.core.repo.interfaces.ChannelsRepository
import github.alexzhirkevich.community.core.repo.interfaces.StorageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

class ChannelViewModel @AssistedInject constructor(
    @Assisted id : String,
    @Assisted globalAudioPlayer: AudioPlayer,
    @Assisted globalStorageRepository: StorageRepository,
    @Stage private val  channelRepository : ChannelsRepository,
    @Stage taggableRepository: TaggableRepository,
    messageBuilder: MessageBuilder<Post>,
) : ChannelDetailViewModel(id, globalStorageRepository,globalAudioPlayer,
        channelRepository, taggableRepository),
    AudioPlayer by globalAudioPlayer,
    StorageRepository by globalStorageRepository,
    MessageBuilder<Post> by messageBuilder {

    companion object {
        fun provideFactory(
            factory: Factory,
            id: String,
            audioPlayer: AudioPlayer,
            storageRepository: StorageRepository
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    return factory.create(id,audioPlayer,storageRepository) as T
                }
            }
    }
    @AssistedFactory
    interface Factory {
        fun create(
            id : String,
            audioPlayer: AudioPlayer,
            storageRepository: StorageRepository
        ) : ChannelViewModel
    }

    private val channelJob : Job?=null
    private val subJob : Job?= null
    private val adminJob : Job?= null

    fun createPost(post: Post) = viewModelScope.launch(Dispatchers.IO) {
        channelRepository.postsRepository.create(post)
    }


    val posts: Flow<DataState<List<SendableWrap>>>
        = channelRepository.postsRepository
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
                    is Post -> SendableWrap.PostWrap(
                        post = msg,
                        channel = data.value.valueOrNull(),
                        121232133L,
                        213123L,
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

    }.apply {
        launchIn(viewModelScope + Dispatchers.IO)
    }

    init {
        update()
    }
}

