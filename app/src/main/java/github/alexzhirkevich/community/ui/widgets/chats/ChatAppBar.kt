package github.alexzhirkevich.community.ui.widgets.chats

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.skydoves.landscapist.glide.GlideImage
import github.alexzhirkevich.community.toPlaceholderText
import github.alexzhirkevich.community.ui.theme.Colors
import github.alexzhirkevich.community.ui.theme.Dimens
import github.alexzhirkevich.community.ui.widgets.common.AvatarTextPlaceholder
import github.alexzhirkevich.community.ui.widgets.common.NavigateBackIconButton

@Preview
@Composable
private fun f(){

}

@Composable
fun ChatAppBar(
    navController: NavController,
    imageUri : String = "",
    name : String = "",
    description : String = "",
    color : Color = Colors.Black,
    onClick : () -> Unit = {}
){

    val density = LocalDensity.current

    val requestOptions = remember {
        RequestOptions()
            .centerCrop()
            .encodeQuality(50)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .override(with(density){
                Dimens.AvatarSizeSmall.roundToPx()
            })
    }

    TopAppBar {
        NavigateBackIconButton(navController = navController)

        val placeHolder = @Composable {
            AvatarTextPlaceholder(
                text = name.toPlaceholderText(),
                color = color,
                fontSize = Dimens.FontSizeMedium
            )
        }

        GlideImage(
            imageModel = imageUri,
            modifier = Modifier
                .size(Dimens.AvatarSizeSmall)
                .clip(CircleShape)
                .border(Dimens.AvatarBorderSize, Color.White, CircleShape),
            requestOptions = requestOptions,
            failure = { placeHolder() },
            loading = { placeHolder() }
        )
        
        Spacer(modifier = Modifier.width(10.dp))
        
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = name,
                fontSize = Dimens.FontSizeMedium
            )
            Text(
                text = description,
                fontSize = Dimens.FontSizeMedium,
                color = Colors.Gray
            )
        }
    }
}