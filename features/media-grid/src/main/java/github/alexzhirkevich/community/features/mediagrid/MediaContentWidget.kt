package github.alexzhirkevich.community.features.mediagrid

import android.net.Uri
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.request.RequestOptions
import com.skydoves.landscapist.glide.GlideImage
import github.alexzhirkevich.community.core.Loading
import github.alexzhirkevich.community.core.entities.MediaContent
import github.alexzhirkevich.community.core.orNone
import github.alexzhirkevich.community.core.repo.interfaces.StorageDownloader
import kotlinx.coroutines.flow.collect


@Composable
fun MediaContentWidget (
    mediaContent: MediaContent,
    storageDownloader: StorageDownloader,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    var resource by remember {
        mutableStateOf(
            storageDownloader.isDownloaded(mediaContent)?.let {
                Loading.Success(it)
            }.orNone()
        )
    }

    var needDownload by remember {
        mutableStateOf(
            storageDownloader.isDownloading(mediaContent.url)
        )
    }

    LaunchedEffect(key1 = needDownload) {
        if (resource is Loading.Success)
            return@LaunchedEffect

        if (needDownload) {
            (if (mediaContent.type == MediaContent.IMAGE)
                storageDownloader.downloadImage(mediaContent.url)
            else storageDownloader.downloadVideo(mediaContent.url))
                .collect {
                    resource = it
                }
        } else {
            storageDownloader.cancelDownload(mediaContent.url)
        }
    }


    val width = mediaContent.width
    val height = mediaContent.height

    Box(
        modifier = modifier
            .let {
                if (width != null && height != null) {
                    it.aspectRatio(width.toFloat() / height)
                } else it
            }
            .clickable {
                needDownload = true
                if (resource is Loading.Success) {
                    onClick()
                }
            }
    ) {

        val res = resource

        if (res !is Loading.Success) {
            Thumbnail(mediaContent)
        }

        when (res) {
            is Loading.None, is Loading.Error -> {
                Metadata(mediaContent.size)
                DownloadIcon {
                    needDownload = true
                }
            }
            is Loading.Progress<*> -> {
                Metadata(res)
                ProgressBar(res) {
                    needDownload = false
                }
            }
            is Loading.Success -> {
                GlideImage(
                    imageModel = res.value,
                    requestOptions = { RequestOptions().override(768, 432) },
                    modifier = (if (mediaContent.width != null && mediaContent.height != null)
                        Modifier.fillMaxSize() else Modifier).clickable(onClick = onClick),
                )

                if (mediaContent.type == MediaContent.VIDEO) {
                    PlayIcon(onClick)
                }
            }
        }
    }
}

private fun formatSize(size : Long) : Pair<Float,String>{
    var sSize: String

    val total = size.let {
        when {
            it > 1024 * 1024 * 1024 -> {
                sSize = "Gb"
                it / 1024f / 1024f / 1024f
            }
            it > 1024 * 1024 -> {
                sSize = "Mb"
                it / 1024f / 1024f
            }
            it > 1024 -> {
                sSize = "Kb"
                it / 1024f
            }
            else -> {
                sSize = "B"
                it.toFloat()
            }
        }
    }
    return total to sSize
}

@Composable
private fun BoxScope.Metadata(totalBytes : Long) {

    val (total, sSize) = formatSize(totalBytes)

    Text(
        text = "%.2f $sSize".format(total),
        modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(10.dp)
            .background(
                color = MaterialTheme.colors.background.copy(alpha = .5f),
                shape = RoundedCornerShape(25)
            )
            .padding(5.dp)
    )
}

@Composable
private fun BoxScope.Metadata(loading: Loading.Progress<*>) {

    val (total, sSize) = formatSize(loading.totalBytes)
    val current = total * loading.value

    Text(
        text = "%.2f / %.2f $sSize".format(current,total),
        modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(10.dp)
            .background(
                color = MaterialTheme.colors.background.copy(alpha = .5f),
                shape = RoundedCornerShape(25)
            )
            .padding(5.dp)

    )
}

@Composable
private fun Thumbnail(mediaContent: MediaContent){
    if (mediaContent.thumbnailUrl != null) {
        GlideImage(
            modifier = Modifier.fillMaxSize(),
            imageModel = mediaContent.thumbnailUrl,
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
private fun BoxScope.PlayIcon(onClick: () -> Unit){
    Icon(
        modifier = Modifier
            .size(50.dp)
            .align(Alignment.Center)
            .clickable(onClick = onClick)
            .clip(CircleShape),
        imageVector = Icons.Rounded.PlayArrow,
        tint = MaterialTheme.colors.surface,
        contentDescription = "Play"
    )
}

@Composable
private fun BoxScope.DownloadIcon(onClick : () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(45.dp)
            .align(Alignment.Center)
            .clip(CircleShape)
    ) {
        Icon(
            imageVector = Icons.Rounded.Download,
            tint = MaterialTheme.colors.surface,
            contentDescription = "Download"
        )
    }
}

@Composable
private fun BoxScope.ProgressBar(loading : Loading.Progress<*>,onCancel : () -> Unit){

    val animatedProgress by animateFloatAsState(targetValue = loading.value)

    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    CircularProgressIndicator(
        modifier = Modifier
            .size(45.dp)
            .align(Alignment.Center)
            .rotate(rotation),
        progress = animatedProgress,
        color = MaterialTheme.colors.surface
    )
    IconButton(
        modifier = Modifier
            .size(45.dp)
            .clip(CircleShape)
            .align(Alignment.Center),
        onClick = onCancel) {
        Icon(
            imageVector = Icons.Rounded.Close,
            tint = MaterialTheme.colors.surface,
            contentDescription = "Close"
        )
    }
}