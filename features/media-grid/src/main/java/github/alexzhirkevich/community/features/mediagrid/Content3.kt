package github.alexzhirkevich.community.features.mediagrid

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import github.alexzhirkevich.community.core.entities.MediaContent
import github.alexzhirkevich.community.core.repo.interfaces.StorageDownloader

@Composable
internal fun Content3(
    content: List<MediaContent>,
    storageDownloader: StorageDownloader,
    modifier: Modifier = Modifier,
    onClick: (MediaContent) -> Unit
){
    val width1 = (content[0].width ?: 1).toFloat()
    val height1 = (content[0].height ?: 1).toFloat()
    val width2 = (content[1].width ?: 1).toFloat()
    val height2 = (content[1].height ?: 1).toFloat()
    val width3 = (content[2].width ?: 1).toFloat()
    val height3 = (content[2].height ?: 1).toFloat()

    val aspect1 = width1 / height1
    val aspect2 = width2 / height2
    val aspect3 = width3 / height3

    Box(
        modifier = modifier
    ) {
        when {
            aspect1 >= 1 && aspect2 >= 1 && aspect3 > 1 -> {
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

            aspect1 >= aspect2 + aspect3 -> {
                Column {
                    MediaContentWidget(
                        mediaContent = content[0],
                        storageDownloader = storageDownloader,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        onClick(content[0])
                    }
                    Content2(
                        content = listOf(content[1], content[2]),
                        storageDownloader = storageDownloader,
                        onClick = onClick
                    )
                }
            }

            else -> {
                val columnAspect = calculateComboAspectRadio(width2, height2, width3, height3, true)

                Row() {
                    MediaContentWidget(
                        mediaContent = content[0],
                        storageDownloader = storageDownloader,
                        modifier = Modifier
                            .weight((aspect1 + columnAspect) * aspect1)
                    ) {
                        onClick(content[0])
                    }
                    Column(
                        modifier = Modifier
                            .weight((aspect1 + columnAspect) * columnAspect)
                    ) {
                        MediaContentWidget(
                            mediaContent = content[1],
                            storageDownloader = storageDownloader,
                            modifier = Modifier
                                .weight((aspect2 + aspect3) * aspect3, false)
                        ) {
                            onClick(content[1])
                        }
                        MediaContentWidget(
                            mediaContent = content[2],
                            storageDownloader = storageDownloader,
                            modifier = Modifier
                                .weight((aspect2 + aspect3) * aspect2, false)
                        ) {
                            onClick(content[2])
                        }
                    }
                }
            }

//        else -> {
//            Row {
//
//                var columnAspect by remember {
//                    mutableStateOf(1f)
//                }
//
//                MediaContentWidget(
//                    mediaContent = content[0],
//                    storageDownloader = storageDownloader,
//                    modifier = Modifier
//                        .weight((aspect1 + columnAspect) * aspect1)
//                ) {
//                    onClick(content[0])
//                }
//                Box(
//                    modifier = Modifier
//                        .weight((aspect1 + columnAspect) * columnAspect)
//                ) {
//                    columnAspect = Content2(
//                        content = listOf(content[1], content[2]),
//                        storageDownloader = storageDownloader,
//                        onClick = onClick
//                    )
//                }
//            }
//        }
        }
    }
}