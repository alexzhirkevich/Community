package github.alexzhirkevich.community.screens.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Reply
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.skydoves.landscapist.glide.GlideImage
import github.alexzhirkevich.community.common.ChatWrap
import github.alexzhirkevich.community.common.SendableWrap
import github.alexzhirkevich.community.common.composable.toPlaceholderText
import github.alexzhirkevich.community.common.util.dateTime
import github.alexzhirkevich.community.features.aft.AutoFormatText
import github.alexzhirkevich.community.theme.Colors
import github.alexzhirkevich.community.theme.Dimens
import github.alexzhirkevich.community.common.composable.AvatarTextPlaceholder
import github.alexzhirkevich.community.common.composable.spreadingBackground
import github.alexzhirkevich.community.core.entities.interfaces.Channel
import github.alexzhirkevich.community.features.aft.TagNavigator


@Composable
internal fun PostWidget(
    channel : ChatWrap.ChannelWrap,
    post : SendableWrap.PostWrap,
    isSelectionEnabled : MutableState<Boolean>,
    selectedItems: SnapshotStateList<String>,
    tagNavigator: TagNavigator
){
    val density = LocalDensity.current

    val isSelected = remember {
        mutableStateOf(isSelectionEnabled.value && post.id in selectedItems)
    }

    val animationEnabled = remember {
        mutableStateOf(false)
    }

    val requestOptions = remember {
        RequestOptions()
            .centerCrop()
            .encodeQuality(50)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .override(with(density) {
                Dimens.AvatarSizeSmall.roundToPx()
            })
    }

    val touchPoint = remember {
        mutableStateOf(Offset.Zero)
    }


    val toggleSelection  = remember {
        {
            isSelected.value = !isSelected.value
            if (isSelected.value) {
                selectedItems.add(post.id)
            } else {
                selectedItems.remove(post.id)
            }
            isSelectionEnabled.value = selectedItems.isNotEmpty()
        }
    }

    LaunchedEffect(key1 = isSelectionEnabled.value) {
        if (!isSelectionEnabled.value && isSelected.value) {
            animationEnabled.value = false
            isSelected.value = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .spreadingBackground(
                MaterialTheme.colors.secondaryVariant,
                animationEnabled = animationEnabled.value,
                visible = isSelected.value,
                fromOffset = touchPoint.value
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        animationEnabled.value = true
                        touchPoint.value = it
                        toggleSelection()
                    },
                    onPress = {
                        val beforeRelease = isSelected.value
                        if (tryAwaitRelease() && isSelectionEnabled.value &&
                            beforeRelease == isSelected.value
                        ) {
                            animationEnabled.value = true
                            touchPoint.value = it
                            toggleSelection()
                        }
                    }
                )
            }
            .padding(
                horizontal = 10.dp,
                vertical = 15.dp
            ) ,
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
                    requestOptions = { requestOptions },
                    failure = { PlaceHolder(channel) },
                    loading = { PlaceHolder(channel) }
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = channel.name,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    fontSize = MaterialTheme.typography.body1.fontSize,
                    color = MaterialTheme.colors.secondaryVariant,
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
                        style = MaterialTheme.typography.body2
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
                    tint = MaterialTheme.colors.secondaryVariant
                )

                Spacer(modifier = Modifier.width(3.dp))

                PostInfoText(text = post.viewsCount.toString())

                Spacer(modifier = Modifier.width(10.dp))

                Icon(
                    modifier = iconModifier,
                    imageVector = Icons.Filled.Reply,
                    tint = MaterialTheme.colors.secondaryVariant,
                    contentDescription = "Shares"
                )

                Spacer(modifier = Modifier.width(3.dp))

                PostInfoText(text = post.sharesCount.toString())

                Spacer(modifier = Modifier.width(10.dp))

                PostInfoText(text = post.time.dateTime().time)
            }
        }


        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val context = LocalContext.current

            if (post.text != null) {
                AutoFormatText(
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
                    style = MaterialTheme.typography.body1,
                    linkColor = Colors.Link,
                    tagNavigator = tagNavigator
                )
            }
        }
    }
}

@Composable
fun  PlaceHolder(channel : Channel) {
    AvatarTextPlaceholder(
        text = channel.name.toPlaceholderText(),
        color = Colors.Channels,
        fontSize = MaterialTheme.typography.body1.fontSize,
        modifier = Modifier
            .fillMaxSize()
            .clip(CircleShape)
            .background(color = Colors.White)
    )
}