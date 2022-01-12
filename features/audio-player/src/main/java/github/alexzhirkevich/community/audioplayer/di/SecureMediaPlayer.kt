package github.alexzhirkevich.community.audioplayer.di

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.FileDescriptor
import java.net.HttpCookie

private enum class MediaPlayerState{
    IDLE,
    PREPARING,
    PREPARING_FOR_PLAY,
    PREPARED,
    INITIALIZED,
    STARTED,
    STOPPED,
    PAUSED,
    COMPLETED,
    ERROR,
    RELEASED
}

class SecureMediaPlayer : MediaPlayer() {

    private var mediaPlayerState = MutableStateFlow(MediaPlayerState.IDLE)
    private var onPreparedListener: OnPreparedListener? = null
    private var onErrorListener: OnErrorListener? = null
    private var onCompletionListener: OnCompletionListener? = null
    private var onResetListener: (() -> Unit)? = null
    private var onStopListener: (() -> Unit)? = null
    private var onStartListener: (() -> Unit)? = null
    private var onPauseListener: (() -> Unit)? = null

    init {
        super.setOnPreparedListener {
            onPrepared()
        }

        super.setOnErrorListener { mp, what, extra ->
            onError(mp, what, extra)
        }
        super.setOnCompletionListener {
            onCompleted()
        }
    }

    override fun setOnPreparedListener(listener: OnPreparedListener?) {
        this.onPreparedListener = listener
    }

    override fun setOnErrorListener(listener: OnErrorListener?) {
        this.onErrorListener = listener
    }

    override fun setOnCompletionListener(listener: OnCompletionListener?) {
        this.onCompletionListener = listener
    }

    fun setOnResetListener(onReset: () -> Unit) {
        onResetListener = onReset
    }

    override fun reset() {
        setState {
            if (it != MediaPlayerState.RELEASED) {
                super.reset()
                onResetListener?.invoke()
                MediaPlayerState.IDLE
            } else it
        }
    }

    override fun prepareAsync() {
        prepareInternal(false, true)
    }

    fun prepareAsyncAndPlay() {
        prepareInternal(true, true)
    }

    override fun prepare() {
        prepareInternal(false, false)
    }

    fun prepareAndPlay() {
        prepareInternal(true, false)
    }

    override fun start() {
        prepareInternal(true,true,onStartListener)
    }

    override fun pause() {
        setState {
            when (it) {
                MediaPlayerState.RELEASED -> it
                MediaPlayerState.STARTED -> kotlin.runCatching {
                    super.pause()
                    onPauseListener?.invoke()
                    MediaPlayerState.PAUSED
                }.getOrDefault(it)
                MediaPlayerState.COMPLETED -> {
                    MediaPlayerState.PAUSED
                }
                else -> it
            }
        }
    }

    override fun stop() {
        setState {
            when (it) {
                MediaPlayerState.COMPLETED, MediaPlayerState.STOPPED -> {
                    MediaPlayerState.STOPPED
                }
                in listOf(
                    MediaPlayerState.PREPARED,
                    MediaPlayerState.STARTED,
                    MediaPlayerState.PAUSED,
                ) -> kotlin.runCatching {
                    super.stop()
                    onStopListener?.invoke()
                    MediaPlayerState.STOPPED
                }.getOrDefault(it)
                else -> it
            }
        }
    }

    override fun seekTo(msec: Long, mode: Int) {
        if (mediaPlayerState.value !in listOf(
                MediaPlayerState.IDLE,
                MediaPlayerState.INITIALIZED,
                MediaPlayerState.STOPPED,
                MediaPlayerState.ERROR
            )
        ) kotlin.runCatching {
            super.seekTo(msec, mode)
        }
    }

    override fun release() {
        setState {
            if (it != MediaPlayerState.RELEASED) {
                MediaPlayerState.RELEASED.also { super.release() }
            } else it
        }
    }

    override fun getDuration(): Int {
        return if (mediaPlayerState.value !in listOf(
                MediaPlayerState.ERROR,
                MediaPlayerState.IDLE,
                MediaPlayerState.INITIALIZED,
                MediaPlayerState.RELEASED,
            )
        ) kotlin.runCatching { super.getDuration() }.getOrDefault(0)
        else 0
    }

    override fun getVideoHeight(): Int {
        return if (mediaPlayerState.value !in listOf(
                MediaPlayerState.ERROR,
                MediaPlayerState.RELEASED,
        )) kotlin.runCatching { super.getVideoHeight() }.getOrDefault(0)
        else 0
    }

    override fun isPlaying(): Boolean {
        return if (mediaPlayerState.value !in listOf(
                MediaPlayerState.ERROR,
                MediaPlayerState.RELEASED,
            )
        ) kotlin.runCatching { super.isPlaying() }.getOrDefault(false)
        else false
    }

    override fun getVideoWidth(): Int {
        return if (mediaPlayerState.value !in listOf(
                MediaPlayerState.ERROR,
                MediaPlayerState.RELEASED,
            )) kotlin.runCatching { super.getVideoWidth() }.getOrDefault(0)
        else 0
    }

    override fun getCurrentPosition(): Int {
        return if (mediaPlayerState.value !in listOf(
                MediaPlayerState.ERROR,
                MediaPlayerState.RELEASED,
            )
        ) kotlin.runCatching { super.getCurrentPosition() }.getOrDefault(0)
        else 0
    }

    override fun attachAuxEffect(effectId: Int) {
        setState {
            if (it !in listOf(
                    MediaPlayerState.IDLE,
                    MediaPlayerState.ERROR,
                    MediaPlayerState.RELEASED,
                )
            ) {
                kotlin.runCatching {
                    super.attachAuxEffect(effectId)
                }
            }
            it
        }
    }

    override fun setAudioAttributes(attributes: AudioAttributes?) {
        if (mediaPlayerState.value !in listOf(
                MediaPlayerState.ERROR,
                MediaPlayerState.RELEASED
            )
        ) {
            kotlin.runCatching {
                super.setAudioAttributes(attributes)
            }
        }
    }

    override fun setAudioSessionId(sessionId: Int) {
        when (mediaPlayerState.value) {
            MediaPlayerState.IDLE ->
                kotlin.runCatching { super.setAudioSessionId(sessionId) }
            MediaPlayerState.RELEASED -> {}
            else ->{
                reset()
                kotlin.runCatching {
                    audioSessionId = sessionId
                }
            }

        }
    }

    override fun setAudioStreamType(streamtype: Int) {
        if (mediaPlayerState.value !in listOf(
                MediaPlayerState.ERROR,
                MediaPlayerState.RELEASED
            )
        ) {
            kotlin.runCatching {
                super.setAudioStreamType(streamtype)
            }
        }
    }

    override fun setDataSource(fd: FileDescriptor?, offset: Long, length: Long) {
        setState {
            when {
                it == MediaPlayerState.IDLE -> {
                    kotlin.runCatching {
                        super.setDataSource(fd, offset, length)
                        MediaPlayerState.INITIALIZED
                    }.getOrDefault(it)
                }
                it != MediaPlayerState.RELEASED -> {
                    reset()
                    kotlin.runCatching {
                        super.setDataSource(fd, offset, length)
                        MediaPlayerState.INITIALIZED
                    }.getOrDefault(it)
                }
                else -> it
            }
        }
    }

    override fun setVideoScalingMode(mode: Int) {
        if (mediaPlayerState.value !in listOf(
                MediaPlayerState.IDLE,
                MediaPlayerState.ERROR,
                MediaPlayerState.RELEASED,
        )) {
            kotlin.runCatching {
                super.setVideoScalingMode(mode)
            }
        }
    }

    override fun setLooping(looping: Boolean) {
        if (mediaPlayerState.value !in listOf(
                MediaPlayerState.ERROR,
                MediaPlayerState.RELEASED,
            )) {
            kotlin.runCatching {
                super.setLooping(looping)
            }
        }
    }

    private fun prepareInternal(play: Boolean, async: Boolean, onSuccess :(() -> Unit)?=null) {
        setState {
            when(it) {
                in listOf(
                    MediaPlayerState.INITIALIZED,
                    MediaPlayerState.STOPPED,
                ) -> kotlin.runCatching {
                    if (async)
                        super.prepareAsync()
                    else super.prepare()
                    if (play)
                        MediaPlayerState.PREPARING_FOR_PLAY
                    else MediaPlayerState.PREPARING
                }.onSuccess {
                    onSuccess?.invoke()
                }.getOrDefault(it)

                MediaPlayerState.PREPARING -> {
                    if (play)
                        MediaPlayerState.PREPARING_FOR_PLAY
                    else it
                }

                else -> it
            }
        }
    }

    private fun setState(
        synchronized: Boolean = true,
        state: (current: MediaPlayerState) -> MediaPlayerState
    )
            : Boolean {
        return if (synchronized) {
            synchronized(mediaPlayerState) {
                setStateUnsafe(state)
            }
        } else setStateUnsafe(state)
    }

    private fun setStateUnsafe(
        state: (current: MediaPlayerState) -> MediaPlayerState
    ): Boolean {
        val new = state(mediaPlayerState.value)
        return if (mediaPlayerState.value != new) {
            mediaPlayerState.tryEmit(state(mediaPlayerState.value))
            true
        } else false
    }

    private fun onCompleted() {
        setState {
            MediaPlayerState.COMPLETED
        }
        onCompletionListener?.onCompletion(this)
    }

    private fun onError(mp: MediaPlayer, what: Int, extra: Int): Boolean {
        setState {
            MediaPlayerState.ERROR
        }
        return onErrorListener?.onError(mp, what, extra) ?: false
    }

    private fun onPrepared() {
        when (mediaPlayerState.value) {
            MediaPlayerState.PREPARING_FOR_PLAY -> {
                start()
            }
            MediaPlayerState.PREPARING -> {
                setState {
                    if (it == MediaPlayerState.PREPARING)
                        MediaPlayerState.PREPARED
                    else it
                }
            }
        }
        onPreparedListener?.onPrepared(this)
    }
}