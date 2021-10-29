package github.alexzhirkevich.community.ui.widgets.chats

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.skydoves.landscapist.glide.GlideImage
import github.alexzhirkevich.community.common.util.dateTime
import github.alexzhirkevich.community.toPlaceholderText
import github.alexzhirkevich.community.ui.theme.Colors
import github.alexzhirkevich.community.ui.theme.Dimens
import github.alexzhirkevich.community.ui.widgets.common.AvatarTextPlaceholder

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun ChatWidget(
    id : String,
    avatarUrl: String,
    name: String,
    secondLine: String,
    time: Long,
    unreadCount: Int,
    unreadBackground: Color,
    isSelectionEnabled: MutableState<Boolean>,
    selectedIds : SnapshotStateList<String>,
    onClick: () -> Unit,
    ) {

    val isSelected = remember {
        mutableStateOf(id in selectedIds)
    }

    if (!isSelectionEnabled.value){
        isSelected.value = false
    }

    val toggleSelection = remember {
        {

            if (!isSelected.value) {
                selectedIds.add(id)
            } else {
                selectedIds.remove(id)
            }
            isSelectionEnabled.value = selectedIds.isNotEmpty()
            isSelected.value = !isSelected.value
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick =
                if (!isSelectionEnabled.value)
                    onClick
                else
                    toggleSelection,
                onLongClick = toggleSelection
            )

    ) {
        Row(
            modifier = Modifier
                .padding(
                    horizontal = Dimens.ChatsPagerMargin,
                    vertical = Dimens.AvatarPadding
                )
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,

            ) {
            Box {

                val density = LocalDensity.current

                val requestOptions = remember {
                    RequestOptions()
                        .centerCrop()
                        .encodeQuality(50)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .override(with(density){
                            Dimens.AvatarSize.roundToPx()
                        })
                }

                val placeHolder = @Composable {
                    AvatarTextPlaceholder(
                        text = name.toPlaceholderText(),
                        color = unreadBackground,
                        fontSize = Dimens.FontSizeBig
                    )
                }

                GlideImage(
                    imageModel = avatarUrl,
                    modifier = Modifier
                        .size(Dimens.AvatarSize)
                        .clip(CircleShape)
                        .border(Dimens.AvatarBorderSize, Color.White, CircleShape),
                    requestOptions = requestOptions,
                    loading = {
                        placeHolder()
                    },
                    failure = {
                        placeHolder()
                    }
                )
                androidx.compose.animation.AnimatedVisibility(
                    modifier = Modifier
                        .align(Alignment.BottomEnd),
                    visible = isSelectionEnabled.value && isSelected.value,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {

                    Icon(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(20.dp)
                            .background(Colors.Black)
                            .border(
                                width = 1.dp,
                                shape = CircleShape,
                                color = Colors.Black
                            ),
                        imageVector = Icons.Rounded.CheckCircle,
                        tint = Colors.White,
                        contentDescription = "Selected State"
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier
                        .absolutePadding(left = Dimens.AvatarPadding * 2)
                        .weight(1f),
                ) {

                    Text(
                        text = name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = Dimens.FontSizeBig
                    )
                    Text(
                        text = secondLine,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = Dimens.FontSizeMedium,
                        color = Colors.TextSecondary
                    )
                }
                Column(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        modifier = Modifier.padding(bottom = 10.dp),
                        text = time.dateTime(LocalContext.current).timeNearly,
                        fontSize = Dimens.FontSizeSmall,
                        color = Colors.TextSecondary
                    )
                    Text(
                        text = unreadCount.toString(),
                        fontSize = Dimens.FontSizeMedium,
                        modifier = Modifier

                            .background(
                                color = if (unreadCount>0) unreadBackground else Color.Transparent,
                                shape = RoundedCornerShape(50)
                            )
                            .alpha(if (unreadCount>0) 1f else 0f)
                            .padding(3.dp)
                    )
                }
            }
        }
    }
}