package github.alexzhirkevich.community.ui.widgets.posts

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Reply
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.skydoves.landscapist.glide.GlideImage
import github.alexzhirkevich.community.common.util.dateTime
import github.alexzhirkevich.community.core.entities.MediaContent
import github.alexzhirkevich.community.data.ChatWrap
import github.alexzhirkevich.community.data.SendableWrap
import github.alexzhirkevich.community.smt.EmptyTranslatedMessage
import github.alexzhirkevich.community.smt.TranslatedMessage
import github.alexzhirkevich.community.smt.translate
import github.alexzhirkevich.community.toPlaceholderText
import github.alexzhirkevich.community.ui.theme.Colors
import github.alexzhirkevich.community.ui.theme.Dimens
import github.alexzhirkevich.community.ui.theme.ifInDarkMode
import github.alexzhirkevich.community.ui.widgets.common.AvatarTextPlaceholder


@Composable
fun PostWidget(
    channel : ChatWrap.ChannelWrap,
    post : SendableWrap.PostWrap,
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

    val placeHolder = @Composable {
        AvatarTextPlaceholder(
            text = channel.name.toPlaceholderText(),
            color = Colors.Channels,
            fontSize = Dimens.FontSizeMedium
        )
    }

    Box(
        modifier = Modifier.fillMaxWidth()
            .padding(
                horizontal = 10.dp,
                vertical = 15.dp
            ),
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
            ) {
                GlideImage(
                    imageModel = channel.imageUri,
                    modifier = Modifier
                        .size(Dimens.AvatarSizeSmall)
                        .clip(CircleShape)
                        .border(Dimens.AvatarBorderSize, Color.White, CircleShape),
                    requestOptions = requestOptions,
                    failure = { placeHolder() },
                    loading = { placeHolder() }
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = channel.name,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    fontSize = Dimens.FontSizeMedium,
                    color = Colors.Gray,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                @Composable
                fun PostInfoText(text: String) {
                    Text(
                        text = text,
                        color = Colors.Gray,
                        fontSize = Dimens.FontSizeSmall,
                    )
                }

                val iconModifier = remember {
                    Modifier.size(with(density) {
                        Dimens.FontSizeSmall.toDp()
                    })
                }
                Icon(
                    modifier = iconModifier,
                    imageVector = Icons.Filled.Visibility,
                    contentDescription = "Views",
                    tint = Colors.Gray
                )

                Spacer(modifier = Modifier.width(3.dp))

                PostInfoText(text = post.viewsCount.toString())

                Spacer(modifier = Modifier.width(10.dp))

                Icon(
                    modifier = iconModifier,
                    imageVector = Icons.Filled.Reply,
                    tint = Colors.Gray,
                    contentDescription = "Shares"
                )

                Spacer(modifier = Modifier.width(3.dp))

                PostInfoText(text = post.sharesCount.toString())

                Spacer(modifier = Modifier.width(10.dp))

                PostInfoText(text = post.time.dateTime(LocalContext.current).time)
            }
        }


        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (post.text != null) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 10.dp + Dimens.AvatarSizeSmall)
                        .padding(
                            top =
                            if (post.content.isNullOrEmpty())
                                Dimens.AvatarSizeSmall + 10.dp
                            else
                                10.dp
                        ),
                    text = post.text!!,
                    fontSize = Dimens.FontSizeMedium,
                    lineHeight = Dimens.FontSizeMedium *1.3
                )
            }
        }
    }
}

@ExperimentalStdlibApi
@Composable
fun PostWidget(
    post : SendableWrap.SystemMessageWrap,
) {
    val translatedMessage by post.translate(LocalContext.current)
        .collectAsState(initial = EmptyTranslatedMessage)

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
                fontSize = Dimens.FontSizeMedium,
                color = Colors.Gray,
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
                            color = ifInDarkMode(Colors.White, Colors.Blue),
                            shape = CircleShape
                        ),
                    imageModel = url,
                    requestOptions = requestOptions
                )
            }
        }
    }
}