package github.alexzhirkevich.community.audioplayer

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import kotlin.math.roundToLong

@Composable
fun VoicePlayer(
    url : Uri,
    voiceDuration : Long,
    audioPlayer: AudioPlayer,
    fontSize : TextUnit,
    fontColor : Color,
    buttonColor : Color = Color.Blue,
    iconColor : Color = Color.White,
    modifier: Modifier = Modifier,
    ) {

    val isPlaying by audioPlayer.isPlaying.collectAsState()
    val playTime by audioPlayer.playTime.collectAsState()
    val playUrl by audioPlayer.currentUri.collectAsState()

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                if (isPlaying)
                    audioPlayer.pause()
                else audioPlayer.play()
            },
            modifier = Modifier
                .clip(CircleShape)
                .size(50.dp)
                .background(color = buttonColor)
        ) {
            Icon(
                imageVector = if (isPlaying)
                    Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                contentDescription = "Play/pause",
                tint = iconColor
            )
        }

        Spacer(modifier = Modifier.width(5.dp))

        AudioSlider(
            uri = url,
            voiceDuration = voiceDuration,
            audioPlayer = audioPlayer,
            color = iconColor,
        )

        Spacer(modifier = Modifier.width(5.dp))

        val textDuration = if (playUrl == url)
            playTime else voiceDuration

        val text = "${textDuration / 1000 / 60}:${(textDuration / 1000) % 60}"

        Text(
            text = text,
            fontSize = fontSize,
            color = fontColor
        )
    }
}

@Composable
fun AudioSlider(
    uri : Uri,
    voiceDuration: Long,
    audioPlayer: AudioPlayer,
    color: Color,
    ) {


    val isPlaying by audioPlayer.isPlaying.collectAsState()
    val playTime by audioPlayer.playTime.collectAsState()
    val currentUri by audioPlayer.currentUri.collectAsState()

    val isActive = isPlaying && currentUri == uri

    Slider(
        value = (if (isActive) playTime else 0).toFloat(),
        valueRange = 0F..voiceDuration.toFloat(),
        onValueChange = {
            if (currentUri != uri) {
                audioPlayer.setAll(uri)
            }

            audioPlayer.play()

            audioPlayer.setPlayTime(it.roundToLong())
        },
        onValueChangeFinished = {
            audioPlayer.stop()
        }
    )
}
