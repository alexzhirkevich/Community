package github.alexzhirkevich.community.features.mediagrid

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import github.alexzhirkevich.community.core.entities.MediaContent
import github.alexzhirkevich.community.core.repo.interfaces.StorageDownloader

@Composable
internal fun Content2(
    content: List<MediaContent>,
    storageDownloader: StorageDownloader,
    modifier: Modifier = Modifier,
    onClick: (MediaContent) -> Unit,
) {

    val width1 = content[0].width?.toFloat() ?: 1f
    val height1 = content[0].height?.toFloat() ?: 1f
    val width2 = content[1].width?.toFloat() ?: 1f
    val height2 = content[1].height?.toFloat() ?: 1f

    val aspect1 = width1 / height1

    val aspect2 = width2 / height2

    Box(modifier = modifier) {
        when {
            aspect1 >= 1 && aspect2 >= 1 -> {
                Column {
                    content.forEach {
                        MediaContentWidget(
                            mediaContent = it,
                            storageDownloader = storageDownloader,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            onClick(it)
                        }
                    }
                }

            }
            else -> {
                Row {
                    MediaContentWidget(
                        mediaContent = content[0],
                        storageDownloader = storageDownloader,
                        modifier = Modifier
                            .weight((aspect1 + aspect2) * aspect1)

                    ) {
                        onClick(content[0])
                    }
                    MediaContentWidget(
                        mediaContent = content[1],
                        storageDownloader = storageDownloader,
                        modifier = Modifier
                            .weight((aspect1 + aspect2) * aspect2)
                    ) {
                        onClick(content[1])
                    }
                }
            }
        }
    }
}