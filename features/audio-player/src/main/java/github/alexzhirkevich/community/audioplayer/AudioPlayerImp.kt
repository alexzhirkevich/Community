package github.alexzhirkevich.community.audioplayer

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSourceFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

//private enum class MediaPlayerState{
//    IDLE,
//    PREPARING,
//    PREPARING_FOR_PLAY,
//    PREPARED,
//    INITIALIZED,
//    STARTED,
//    STOPPED,
//    PAUSED,
//    COMPLETED,
//    ERROR,
//    RELEASED
//}


class AudioPlayerImp @Inject constructor(
   context : Context,
   private val updateScope : CoroutineScope,
) : AudioPlayer, CoroutineScope by updateScope {

    private companion object {
        private const val UPDATE_RATE = 100L
    }

    private var exoPlayer = ExoPlayer.Builder(context).build().apply {
        addListener(object  : Player.Listener{

        })
    }

    private var updateJob: Job? = null

    private val _isPlaying = MutableStateFlow(false)
    override val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    override fun setAll(vararg uri: Uri) {

        uris = uri.toMutableList()
        launch(Dispatchers.Main) {
            exoPlayer.clearMediaItems()
            exoPlayer.setMediaItems(uri.map(MediaItem::fromUri))
            exoPlayer.prepare()
        }
    }

    override fun setCurrent(uri: Uri) {
        val idx = uris.indexOf(uri)
        if (idx != -1) {
            launch(Dispatchers.Main) {
                while (exoPlayer.currentMediaItemIndex != idx) {
                    if (idx > exoPlayer.currentMediaItemIndex)
                        exoPlayer.seekToNextMediaItem()
                    else exoPlayer.seekToPreviousMediaItem()
                }
            }
        } else setAll(uri)
    }

    override fun prev() {
        launch(Dispatchers.Main) {
            exoPlayer.seekToPreviousMediaItem()
        }

    }

    override fun next() {
        launch(Dispatchers.Main) {
            exoPlayer.seekToNextMediaItem()
        }
    }

    override fun play() {
        launch(Dispatchers.Main) {
            exoPlayer.play()
        }
    }

    override fun pause() {
        launch(Dispatchers.Main) {
            exoPlayer.pause()
        }
    }

    override fun stop() {
        launch(Dispatchers.Main) {
            exoPlayer.stop()
        }
    }

    private var uris = mutableListOf<Uri>()
    private val _currentUrl = MutableStateFlow<Uri>(Uri.EMPTY)
    override val currentUri: StateFlow<Uri> = _currentUrl.asStateFlow()

    private val _index = MutableStateFlow(0)
    override val index: StateFlow<Int> = _index.asStateFlow()

    private val _playTime = MutableStateFlow(0L)
    override val playTime: StateFlow<Long> = _playTime.asStateFlow()

    override fun release() {
        exoPlayer.release()
    }

    override fun setPlayTime(playTime: Long, index: Int) {
        exoPlayer.seekTo(index,playTime)
    }


}

