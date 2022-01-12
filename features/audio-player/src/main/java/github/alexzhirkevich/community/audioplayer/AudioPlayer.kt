package github.alexzhirkevich.community.audioplayer

import android.media.AudioAttributes
import android.net.Uri
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Qualifier

interface Player{

    fun setAll(vararg uri: Uri)

    fun setCurrent(uri: Uri)

    fun prev()

    fun next()

    fun play()

    fun pause()

    fun stop()
}

interface StatePlayer : Player {
    val currentUri : StateFlow<Uri>
    val index : StateFlow<Int>
    val isPlaying : StateFlow<Boolean>
}

interface AudioPlayer : StatePlayer{

    val playTime : StateFlow<Long>

    fun release()

    fun setPlayTime(playTime : Long, index : Int = 0)

}

