package github.alexzhirkevich.community.screens.chat.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.outlined.Reply
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.glide.LocalGlideRequestBuilder
import com.skydoves.landscapist.glide.LocalGlideRequestOptions
import github.alexzhirkevich.community.audioplayer.VoicePlayer
import github.alexzhirkevich.community.common.SendableWrap
import github.alexzhirkevich.community.common.composable.HorizontalSide
import github.alexzhirkevich.community.common.composable.horizontalDrag
import github.alexzhirkevich.community.common.composable.spreadingBackground
import github.alexzhirkevich.community.common.composable.toPlaceholderText
import github.alexzhirkevich.community.common.util.dateTime
import github.alexzhirkevich.community.core.entities.interfaces.User
import github.alexzhirkevich.community.screens.chat.data.ChatViewModel
import github.alexzhirkevich.community.theme.Colors
import github.alexzhirkevich.community.theme.Dimens
import github.alexzhirkevich.community.common.composable.AvatarTextPlaceholder
import github.alexzhirkevich.community.core.entities.interfaces.Message
import github.alexzhirkevich.community.core.repo.stage.TestStorageRepository
import github.alexzhirkevich.community.features.aft.AutoFormatText
import github.alexzhirkevich.community.features.aft.TagNavigator
import github.alexzhirkevich.community.features.mediagrid.MediaContentFlexBox

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
internal fun MessageWidget(
    isGroup: Boolean,
    message: SendableWrap.MessageWrap,
    viewModel: ChatViewModel,
    isSelectionEnabled: MutableState<Boolean>,
    selectedItems: SnapshotStateList<String>,
    tagNavigator: TagNavigator,
    previous: SendableWrap? = null,
    next: SendableWrap? = null,
    replyTo: MutableState<SendableWrap.MessageWrap?>,
    modifier: Modifier = Modifier
) {

    val isSameSenderPrev = remember {
        (previous as? SendableWrap.MessageWrap)
            ?.senderId == message.senderId
    }
    val isSameSenderNext = remember {
        (next as? SendableWrap.MessageWrap)
            ?.senderId == message.senderId
    }


    var replyAlpha by remember {
        mutableStateOf(0f)
    }

    var isSelected by remember {
        mutableStateOf(message.id in selectedItems)
    }

    var touchPoint by remember {
        mutableStateOf(Offset.Zero)
    }

    val feedbackFraction = remember {
        Dimens.Message.let {
            it.FeedbackDragDistance / it.MaxDragDistance
        }
    }

    var animationEnabled by remember {
        mutableStateOf(false)
    }

    val toggleSelection  = remember {
        {
            isSelected = !isSelected
            if (isSelected) {
                selectedItems.add(message.id)
            } else {
                selectedItems.remove(message.id)
            }
            isSelectionEnabled.value = selectedItems.isNotEmpty()
        }
    }

    LaunchedEffect(key1 = isSelectionEnabled.value) {
        if (!isSelectionEnabled.value && isSelected) {
            animationEnabled = false
            isSelected = false
        }
    }

    Box(
        modifier = modifier
            .offset(
                x = -if (replyAlpha < feedbackFraction)
                    0.dp
                else Dimens.Message.FeedbackDragDistance *
                        (replyAlpha - feedbackFraction),
                y = 0.dp
            )
            .spreadingBackground(
                MaterialTheme.colors.secondaryVariant,
                animationEnabled = animationEnabled,
                visible = isSelected,
                fromOffset = touchPoint
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        animationEnabled = true
                        touchPoint = it
                        toggleSelection()
                    },
                    onPress = {
                        val beforeRelease = isSelected
                        if (tryAwaitRelease() && isSelectionEnabled.value &&
                            beforeRelease == isSelected
                        ) {
                            animationEnabled = true
                            touchPoint = it
                            toggleSelection()
                        }
                    }
                )
            }
    ) {

        Icon(
            imageVector = Icons.Outlined.Reply,
            contentDescription = "Reply",
            tint = if (replyAlpha < feedbackFraction)
                MaterialTheme.colors.secondaryVariant else Colors.Chats,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 10.dp)
                .alpha(replyAlpha)
                .clip(CircleShape)
                .size(30.dp)
                .fillMaxHeight()
        )

        Box(
            modifier = Modifier
                .horizontalDrag(
                    enabled = !isSelectionEnabled.value,
                    side = HorizontalSide.LEFT,
                    maxOffset = Dimens.Message.MaxDragDistance,
                    effectiveOffset = Dimens.Message.FeedbackDragDistance,
                    hapticFeedback = HapticFeedbackType.LongPress,
                    onDrag = { replyAlpha = it }
                ) {
                   replyTo.value = message
                }
        ) {
            Row(
                modifier = Modifier
                    .align(
                        if (message.isIncoming)
                            Alignment.TopStart
                        else Alignment.TopEnd
                    )
                    .fillMaxWidth()
                    .padding(
//                        start = if (message.isIncoming) Dimens.Message.SmallPadding
//                        else Dimens.Message.BigPadding,
//                        end = if (message.isIncoming) Dimens.Message.BigPadding
//                        else Dimens.Message.SmallPadding,
                        top = 10.dp,
                        bottom = 5.dp
                    ),
                horizontalArrangement = if (message.isIncoming)
                    Arrangement.Start else Arrangement.End
            ) {
                if (isGroup && message.isIncoming) {
                    if (!isSameSenderPrev)
                        MessageSenderAvatar(sender = message.sender)
                    else Spacer(modifier = Modifier.width(Dimens.AvatarSizeSmall))

                    Spacer(modifier = Modifier.width(10.dp))
                }
                if (!message.isIncoming) {
                    MessageIndicator(message = message)
                    Date(
                        time = message.time,
                        modifier = Modifier
                            .align(Alignment.Bottom)
                            .padding(end = 10.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f, fill = false)) {
                    MessageBody(
                        message = message,
                        viewModel = viewModel,
                        isGroup = isGroup,
                        isSameSenderPrev = isSameSenderPrev,
                        messageShape = RoundedCornerShape(Dimens.Message.CornerRadius),
                        tagNavigator = tagNavigator,
                    )
                }
                if (message.isIncoming)
                    Date(
                        time = message.time,
                        modifier = Modifier
                            .align(Alignment.Bottom)
                            .padding(start = 10.dp)
                    )

            }
        }
    }
}

@Composable
internal fun Date(time : Long, modifier: Modifier = Modifier){
    Text(
        text = time.dateTime().time,
        style = MaterialTheme.typography.body2,
        maxLines = 1,
        modifier = modifier
    )
}

@Composable
internal fun RowScope.MessageIndicator(
    message: SendableWrap.MessageWrap
) {

    if (!message.isViewed && !message.hasPendingWrites) {
        Icon(
            imageVector = Icons.Filled.Circle,
            contentDescription = "Isn't viewed",
            tint = Colors.Chats,
            modifier = Modifier
                .size(Dimens.Message.IndicatorSize)
                .align(Alignment.Bottom)

        )
        Spacer(
            modifier = Modifier
                .width(Dimens.Message.IndicatorSize)

        )
    }
    if (message.hasPendingWrites) {
        Icon(
            imageVector = Icons.Outlined.Schedule,
            tint = Colors.Chats,
            contentDescription = "Don't delivered",
            modifier = Modifier
                .size(Dimens.Message.IndicatorSize + Dimens.Message.IndicatorSize / 3)
                .align(Alignment.Bottom)
        )

        Spacer(
            modifier = Modifier
                .width(Dimens.Message.IndicatorSize)
        )
    }
}

@Composable
internal fun MessageBody(
    message: SendableWrap.MessageWrap,
    viewModel: ChatViewModel,
    isGroup: Boolean,
    isSameSenderPrev: Boolean,
    messageShape: Shape,
    tagNavigator: TagNavigator,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = Dimens.Message.Elevation,
        shape = messageShape,
        backgroundColor = if (message.isIncoming)
            Colors.MessageIncoming else Colors.MessageOutComing,
    ) {
        Column(
            modifier = Modifier.padding(3.dp),
            horizontalAlignment = if (message.isIncoming)
                Alignment.Start else Alignment.End
        ) {
            if (isGroup && message.isIncoming && !isSameSenderPrev) {
                Box(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                ) {
                    MessageSenderName(name = message.sender.name)
                }
            }

            message.text?.let {
                Box(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                ) {
                    MessageText(
                        text = it,
                        tagNavigator = tagNavigator,
                    )
                }
            }

            if (message.voice != null) {
                VoicePlayer(
                    url = message.voice!!.url.toUri(),
                    voiceDuration = message.voice!!.len,
                    audioPlayer = viewModel,
                    fontSize =  MaterialTheme.typography.body2.fontSize,
                    fontColor = MaterialTheme.typography.body2.color,
                    buttonColor = Colors.Chats,
                    iconColor = MaterialTheme.colors.primary
                )
            }
            if (!message.content.isNullOrEmpty()) {
                MediaContentFlexBox(
                    mediaContent = message.content!!,
                    storageRepository = TestStorageRepository(LocalContext.current),
                    onContentCLick = {},
                    modifier = Modifier
                        .clip(messageShape)
                        .padding(top = 5.dp)
                )
            }
        }
    }
}

@Composable
internal fun MessageSenderName(name : String){
    Text(
        text = name,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        color = Colors.Chats,
        fontSize = MaterialTheme.typography.body1.fontSize,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(5.dp))
}

@Composable
internal fun MessageText(
    text : String,
    tagNavigator: TagNavigator,
    modifier: Modifier = Modifier
) {

    AutoFormatText(
        modifier = modifier,
        text = text,
        linkColor = Colors.Link,
        tagNavigator = tagNavigator,
        style = MaterialTheme.typography.body1
    )
}

@Composable
internal fun MessageSenderAvatar(sender : User) {

    val density = LocalDensity.current
    val requestOptions = remember {
        RequestOptions()
            .centerCrop()
            .encodeQuality(50)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .override(with(density) {
                Dimens.AvatarSizeSmall.roundToPx()
            })
    }

    val placeHolder = @Composable {
        AvatarTextPlaceholder(
            text = sender.name.toPlaceholderText(),
            color = Colors.Chats,
            fontSize = MaterialTheme.typography.body1.fontSize,
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(color = Colors.White)
        )
    }

    GlideImage(
        imageModel = sender.imageUri,
        modifier = Modifier
            .size(Dimens.AvatarSizeSmall)
            .clip(CircleShape)
            .border(Dimens.AvatarBorderSize, Color.White, CircleShape),
        requestOptions = { requestOptions },
        failure = { placeHolder() },
        loading = { placeHolder() },
    )
}