package github.alexzhirkevich.community

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.InternalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.AndroidEntryPoint
import github.alexzhirkevich.community.common.composable.DetailMenuItemShimmer
import github.alexzhirkevich.community.core.entities.MediaContent
import github.alexzhirkevich.community.core.repo.stage.TestStorageRepository
import github.alexzhirkevich.community.features.mediagrid.MediaContentFlexBox
import github.alexzhirkevich.community.theme.ForcedTheme
import github.alexzhirkevich.community.theme.MainTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlin.math.roundToLong


private val long1 = MediaContent(
    MediaContent.IMAGE,
    url ="https://s1.1zoom.ru/b5050/596/Evening_Forests_Mountains_Firewatch_Campo_Santo_549147_1920x1080.jpg",
    thumbnailUrl = "https://s1.1zoom.ru/b5050/596/Evening_Forests_Mountains_Firewatch_Campo_Santo_549147_1920x1080.jpg",
    size = (2.43 * 1024 * 1024).roundToLong(),
    width = 1920,
    height = 1080
)
private val long2 = MediaContent(
    MediaContent.IMAGE,
    url = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a2/Aspect_ratio_-_4x3.svg/1280px-Aspect_ratio_-_4x3.svg.png",
    thumbnailUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a2/Aspect_ratio_-_4x3.svg/1280px-Aspect_ratio_-_4x3.svg.png",
    size = (2.43 * 1024 * 1024).roundToLong(),
    width = 1280,
    height = 960
)
private val long3 = MediaContent(
    MediaContent.IMAGE,
    url = "https://c4.wallpaperflare.com/wallpaper/214/187/691/video-games-video-game-art-ultrawide-ultra-wide-need-for-speed-heat-hd-wallpaper-preview.jpg",
    thumbnailUrl = "https://c4.wallpaperflare.com/wallpaper/214/187/691/video-games-video-game-art-ultrawide-ultra-wide-need-for-speed-heat-hd-wallpaper-preview.jpg",
    size = (2.43 * 1024 * 1024).roundToLong(),
    width = 3440,
    height = 1080
)
private val high1 = MediaContent(
    MediaContent.IMAGE,
    url ="https://s1.1zoom.ru/b3556/124/Texture_Multicolor_526935_1080x1920.jpg",
    thumbnailUrl = "https://s1.1zoom.ru/b3556/124/Texture_Multicolor_526935_1080x1920.jpg",
    size = (2.43 * 1024 * 1024).roundToLong(),
    width = 1090,
    height = 1920
)
private val high2 = MediaContent(
    MediaContent.IMAGE,
    url = "https://www.earthinpictures.com/world/france/paris/eiffel_tower_960x1280.jpg",
    thumbnailUrl = "https://www.earthinpictures.com/world/france/paris/eiffel_tower_960x1280.jpg",
    size = (2.43 * 1024 * 1024).roundToLong(),
    width = 960,
    height = 1280
)

private val content = listOf(
    long3, long3, high2, long1
)

@Composable
fun Preview(){
    MediaContentFlexBox(
        mediaContent = content,
        storageRepository = TestStorageRepository(LocalContext.current),
        onContentCLick = {})
}


@FlowPreview
@ExperimentalComposeUiApi
@ExperimentalCoroutinesApi
@ExperimentalStdlibApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalPagerApi
@AndroidEntryPoint
@InternalAnimationApi
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            MainTheme.forceTheme(
                theme = ForcedTheme.Dark
            )
            MainTheme.setContent {
                MainScreen()
//                Scaffold() {
//                    DetailMenuItemShimmer()
//                }
            }
        }
    }
}



