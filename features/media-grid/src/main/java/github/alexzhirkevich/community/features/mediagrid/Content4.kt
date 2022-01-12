package github.alexzhirkevich.community.features.mediagrid

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import github.alexzhirkevich.community.core.entities.MediaContent
import github.alexzhirkevich.community.core.repo.interfaces.StorageDownloader
import kotlin.math.abs

@Composable
internal fun Content4(
    content: List<MediaContent>,
    storageDownloader: StorageDownloader,
    modifier : Modifier= Modifier,
    onClick: (MediaContent) -> Unit
){
    val width1 = (content[0].width ?: 1).toFloat()
    val height1 = (content[0].height ?: 1).toFloat()
    val width2 = (content[1].width ?: 1).toFloat()
    val height2 = (content[1].height ?: 1).toFloat()
    val width3 = (content[2].width ?: 1).toFloat()
    val height3 = (content[2].height ?: 1).toFloat()
    val width4 = (content[3].width ?: 1).toFloat()
    val height4 = (content[3].height ?: 1).toFloat()

    val aspect1 = width1 / height1
    val aspect2 = width2 / height2
    val aspect3 = width3 / height3
    val aspect4 = width4 / height4


    Box(modifier) {
        when {
            aspect1 >= 1 && aspect2 >= 1 && aspect3 > 1 && aspect4 > 1 -> {
                Row() {

                    var a1 by remember {
                        mutableStateOf(1f)
                    }

                    var a2 by remember {
                        mutableStateOf(1f)
                    }

                    Content2(
                        content = listOf(content[0],content[2]),
                        storageDownloader = storageDownloader,
                        onClick = onClick,
                        modifier = Modifier
                            .onGloballyPositioned {
                                if (it.size.height != 0) {
                                    a1 = it.size.width.toFloat() / it.size.height
                                }
                            }
                            .weight((a1 + a2) * a1)
                    )
                    Content2(
                        content = listOf(content[1],content[3]),
                        storageDownloader = storageDownloader,
                        onClick = onClick,
                        modifier = Modifier
                            .onGloballyPositioned {
                                if (it.size.height != 0) {
                                    a2 = it.size.width.toFloat() / it.size.height
                                }
                            }
                            .weight((a1 + a2) * a2)
                    )
                }
            }

//            aspect1<=1 && aspect4>=1 -> {
//                Column {
//
//                    var rowAspect by remember {
//                        mutableStateOf(1f)
//                    }
//
//                    Row(modifier =Modifier
//                        .onGloballyPositioned {
//                            if (it.size.height != 0) {
//                                val ra = it.size.width.toFloat() / it.size.height
//                                if (abs(rowAspect-ra)>0.01)
//                                    rowAspect = ra
//                            }
//                        }.weight((rowAspect + aspect4) * rowAspect,false)
//                    ) {
//                        var content2aspect by remember {
//                            mutableStateOf(1f)
//                        }
//
//                        MediaContentWidget(
//                            mediaContent = content[0],
//                            storageDownloader = storageDownloader,
//                            modifier = Modifier
//                                .weight((aspect1 + content2aspect) * aspect1, false)
//                        ) {
//                            onClick(content[0])
//                        }
//                        Content2(
//                            content = listOf(content[1], content[2]),
//                            storageDownloader = storageDownloader,
//                            onClick = onClick,
//                            modifier = Modifier.onGloballyPositioned {
//                                if (it.size.height != 0){
//                                    val c2a = it.size.width.toFloat()/it.size.height
//                                    if (abs(c2a-content2aspect)>0.01)
//                                        content2aspect = it.size.width.toFloat()/it.size.height
//                                }
//                            }.weight((aspect1+content2aspect) * content2aspect)
//                        )
//                    }
//                    MediaContentWidget(
//                        mediaContent = content[3],
//                        storageDownloader = storageDownloader,
//                        modifier = Modifier
//                            .weight((aspect4 + rowAspect) * aspect4, false)
//                    ) {
//                        onClick(content[3])
//                    }
//                }
//            }
            aspect1>1 -> {
                Column() {
                    var content3aspect by remember {
                        mutableStateOf(1f)
                    }

                    if (content3aspect <=0f)
                        content3aspect=1f

                    MediaContentWidget(
                        mediaContent = content[0],
                        storageDownloader = storageDownloader,
                        modifier = Modifier
                            .weight((aspect1+content3aspect)*aspect1,false)
                    ) {
                        onClick(content[0])
                    }
                    Content3(
                        content = listOf(content[1], content[2], content[3]),
                        storageDownloader = storageDownloader,
                        onClick = onClick,
                        modifier = Modifier
                            .onGloballyPositioned {
                                if (it.size.height != 0) {
                                    val c3a = it.size.width.toFloat() / it.size.height
                                    if (abs(c3a-content3aspect)>0.01) {
                                        content3aspect = c3a
                                    }
                                }
                            }
                            .weight((aspect1 + content3aspect) * content3aspect, false)
                    )
                }
            }
            else -> {
                Row() {
                    var content3aspect by remember {
                        mutableStateOf(1f)
                    }

                    if (content3aspect <=0f)
                        content3aspect=1f

                    MediaContentWidget(
                        mediaContent = content[0],
                        storageDownloader = storageDownloader,
                        modifier = Modifier
                            .weight((aspect1+content3aspect)*aspect1,false)
                    ) {
                        onClick(content[0])
                    }
                    Content3(
                        content = listOf(content[1], content[2], content[3]),
                        storageDownloader = storageDownloader,
                        onClick = onClick,
                        modifier = Modifier
                            .onGloballyPositioned {
                                content3aspect = if (it.size.height != 0) {
                                    it.size.width.toFloat() / it.size.height
                                } else 1f
                            }
                            .weight((aspect1 + content3aspect) * content3aspect, false)
                    )
                }
            }
        }
    }
}