package github.alexzhirkevich.community.screens.fullscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import github.alexzhirkevich.community.core.entities.MediaContent
import github.alexzhirkevich.community.screens.fullscreen.data.FullscreenContentViewModel
import github.alexzhirkevich.fullscreen.R

@ExperimentalPagerApi
@Composable
fun FullscreenContentScreen(
    viewModel : FullscreenContentViewModel
){

    val currentIdx = rememberSaveable { 0 }

    Scaffold(
        topBar = {

            val title = if (viewModel.content.size == 1){
                when(val c =viewModel.content[0].type){
                    MediaContent.IMAGE -> stringResource(id = R.string.photo)
                    MediaContent.VIDEO -> stringResource(id = R.string.video)
                    else -> ""
                }
            } else {
                "${currentIdx+1}/${viewModel.content.size}"
            }

//            AppBar(viewModel,viewModel.content[currentIdx],title)
        }
    ) {

        Body(viewModel = viewModel)
    }
}

@Composable
private fun AppBar(
    viewModel: FullscreenContentViewModel,
    currentContent : MediaContent,
    title : String
){
    TopAppBar(
        title = {
            Text(
                text = title
            )
        },
    )
}

@ExperimentalPagerApi
@Composable
private fun Body(
    viewModel: FullscreenContentViewModel
) {
    val state = rememberPagerState()

    Box(
        modifier = Modifier.fillMaxWidth()
            .padding(30.dp)
    ) {

//        MediaContentWidget(
//            mediaContent = MediaContent(
//                MediaContent.IMAGE,
//                "https://firebasestorage.googleapis.com/v0/b/messenger-302121.appspot.com/o/WallpaperDog-20456176.jpg?alt=media&token=7579ea54-247f-4360-8de7-b60ae5802df2",
//                thumbnailUrl = "https://firebasestorage.googleapis.com/v0/b/messenger-302121.appspot.com/o/Webp.net-resizeimage.jpg?alt=media&token=64e3da73-f7f1-48c9-8389-08564c86acfd",
//                size = (2.43 * 1024 * 1024).roundToLong(),
//                width = 7680,
//                height = 4320
//            ),
//            storageDownloader = viewModel
//        ) {
//
//        }
    }

    HorizontalPager(count = viewModel.content.size) { p ->

    }
}

@Composable
fun FullscreenContentPage(
    content: MediaContent,
    viewModel: FullscreenContentViewModel
){
    when(content.type){
        MediaContent.IMAGE ->{
            val loadingState = viewModel.downloadImage(content.url)

        }

    }
}