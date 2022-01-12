package github.alexzhirkevich.community.screens.chat.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.skydoves.landscapist.glide.GlideImage
import github.alexzhirkevich.community.common.SendableWrap
import github.alexzhirkevich.community.core.entities.MediaContent
import github.alexzhirkevich.community.smt.translate
import github.alexzhirkevich.community.theme.Colors
import github.alexzhirkevich.community.theme.Dimens
import github.alexzhirkevich.community.theme.ifInDarkMode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
@ExperimentalStdlibApi
@Composable
fun SystemMessageWidget(
    message : SendableWrap.SystemMessageWrap,
) {
    val translatedMessage by message.translate()

    val text = translatedMessage.text
    val content = translatedMessage.content

    val density = LocalDensity.current

    val requestOptions = remember {
        RequestOptions()
            .centerCrop()
            .encodeQuality(50)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .override(with(density) {
                Dimens.AvatarSize.roundToPx()
            })
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (text.isNotEmpty()) {
            Text(
                modifier = Modifier
                    .padding(
                        vertical = 10.dp,
                        horizontal = 20.dp
                    ),
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.secondaryVariant,
                text = text
            )
        }
        if (!content.isNullOrEmpty()) {
            val url = content.find {
                it.type == MediaContent.IMAGE && it.url.isNotEmpty()
            }?.url

            if (url != null) {
                GlideImage(
                    modifier = Modifier
                        .size(Dimens.AvatarSize)
                        .clip(CircleShape)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colors.surface,
                            shape = CircleShape
                        ),
                    imageModel = url,
                    requestOptions = { requestOptions }
                )
            }
        }
    }
}