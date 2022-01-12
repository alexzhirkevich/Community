package github.alexzhirkevich.messageinput

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.InternalAnimationApi
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.Attachment
import androidx.compose.material.icons.rounded.EmojiEmotions
import androidx.compose.material.icons.rounded.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import github.alexzhirkevich.community.common.SendableWrap
import github.alexzhirkevich.community.core.entities.MediaContent
import github.alexzhirkevich.community.core.entities.MessageBuilder
import github.alexzhirkevich.community.core.entities.interfaces.*
import github.alexzhirkevich.community.core.repo.interfaces.StorageRepository
import github.alexzhirkevich.community.theme.Colors
import github.alexzhirkevich.community.theme.Dimens
import kotlinx.coroutines.CoroutineScope
import kotlin.math.roundToInt

private val ICON_SIZE = 30.dp
private val BUTTON_SIZE = ICON_SIZE+10.dp
private val REPLY_SIZE = 40.dp

@Composable
fun MessageInput(
    builder: MessageBuilder<Message>,
    storageRepository: StorageRepository,
    coroutineScope: CoroutineScope,
    replyTo : MutableState<SendableWrap.MessageWrap?>,
    onSend : (Message) -> Unit = {},
    modifier: Modifier = Modifier,
    visible : Boolean = true,
    content : @Composable () -> Unit
) {
    MessageInputInternal(
        builder = builder,
        storageRepository = storageRepository,
        coroutineScope = coroutineScope,
        replyTo = replyTo.value,
        clearReplyTo = {
            replyTo.value = null
        },
        onSend = {
            replyTo.value = null
            onSend(it)
        },
        modifier = modifier,
        visible = visible,
        content = content
    )
}


@ExperimentalAnimationApi
@InternalAnimationApi
@Composable
fun PostInput(
    builder: MessageBuilder<Post>,
    storageRepository: StorageRepository,
    coroutineScope: CoroutineScope,
    replyTo : MutableState<SendableWrap.PostWrap?>,
    onSend : (Post) -> Unit = {},
    modifier: Modifier = Modifier,
    visible : Boolean = true,
    content : @Composable () -> Unit
) {
    MessageInputInternal(
        builder = builder,
        storageRepository = storageRepository,
        coroutineScope = coroutineScope,
        replyTo = replyTo.value,
        clearReplyTo = {
            replyTo.value = null
        },
        onSend = {
            replyTo.value = null
            onSend(it)
        },
        modifier = modifier,
        visible = visible,
        content = content)
}
@Composable
private inline fun <reified T: Sendable> MessageInputInternal(
    builder: MessageBuilder<T>,
    storageRepository: StorageRepository,
    coroutineScope: CoroutineScope,
    replyTo : SendableWrap?,
    noinline clearReplyTo : () -> Unit,
    crossinline onSend : (T) -> Unit = {},
    modifier: Modifier = Modifier,
    visible : Boolean = true,
    content : @Composable () -> Unit
) {


    val uiScope = rememberCoroutineScope()

    var inputSize by remember {
        mutableStateOf(0)
    }

    val replySize = with(LocalDensity.current) {
        remember {
            REPLY_SIZE.toPx().roundToInt()
        }
    }
    val animatedReplyHeight by animateIntAsState(
        targetValue = if (replyTo != null) replySize else 0
    )

    with(LocalDensity.current) {
        Column(modifier) {

            Box(
                modifier = Modifier.weight(1f)
            ) {
                content()
            }

            if (!visible) {
                inputSize = 0

                return
            }

            var lastReply by remember { mutableStateOf(replyTo) }

            SideEffect {
                if (lastReply != replyTo && replyTo != null) {
                    lastReply = replyTo
                }
            }

            if (lastReply != null) {
                ReplyToWidget(
                    replyTo = lastReply!!,
                    onCancel = clearReplyTo,
                    modifier = Modifier
                        .height(animatedReplyHeight.toDp())

                )
            }

            InputWidget(
                builder = builder,
                storageRepository = storageRepository,
                coroutineScope = coroutineScope,
                replyTo = replyTo,
                onSend = onSend
            )
        }
    }
}

@Composable
private inline fun <reified T: Sendable> InputWidget(
    builder: MessageBuilder<T>,
    storageRepository: StorageRepository,
    coroutineScope: CoroutineScope,
    modifier: Modifier = Modifier,
    replyTo : SendableWrap? =null,
    crossinline onSend : (T) -> Unit = {},
){
    var inputState by remember {
        mutableStateOf(TextFieldValue(""))
    }

    Row(
        modifier = modifier
            .background(color = Colors.DarkGray)
            .padding(1.dp),
        verticalAlignment = CenterVertically
    ) {
        IconButton(
            onClick = { },
            modifier = Modifier
                .padding(3.dp)
                .size(BUTTON_SIZE)
        ) {
            Icon(
                imageVector = Icons.Rounded.EmojiEmotions,
                contentDescription = "Emoji",
                tint = MaterialTheme.colors.secondaryVariant,
                modifier = Modifier
                    .size(ICON_SIZE)
            )
        }
        Box(
            Modifier
                .padding(1.dp)
                .weight(1f)
        ) {

            BasicTextField(
                modifier = Modifier
                    .align(CenterStart)
                    .fillMaxWidth(),
                value = inputState,
                textStyle = LocalTextStyle.current,
                onValueChange = { inputState = it },
                maxLines = 5,
                cursorBrush = SolidColor(Colors.Gray)
            )
            if (inputState.text.isEmpty()) {
                Text(
                    text = stringResource(R.string.message_hint),
                    color = MaterialTheme.colors.secondaryVariant,
                    modifier = Modifier.align(CenterStart)
                )
            }
        }
        IconButton(
            onClick = { },
            modifier = Modifier
                .padding(3.dp)
                .size(BUTTON_SIZE)

        ) {
            Icon(
                imageVector = Icons.Rounded.Attachment,
                contentDescription = "Attach",
                tint = MaterialTheme.colors.secondaryVariant,
                modifier = Modifier
                    .size(ICON_SIZE)

            )
        }
        IconButton(
            onClick = {
                val msg = builder
                    .setReplyTo(replyTo as? T?)
                    .setText(inputState.text.trim())
                    .build()

                onSend(msg)
                inputState = TextFieldValue()
            },
            modifier = Modifier
                .padding(3.dp)
                .size(BUTTON_SIZE)

        ) {
            Icon(
                imageVector = Icons.Rounded.Send,
                contentDescription = "Send",
                tint = MaterialTheme.colors.secondaryVariant,
                modifier = Modifier
                    .size(ICON_SIZE)
            )
        }
    }
}

@Composable
private fun ReplyToWidget(
    replyTo: SendableWrap,
    modifier: Modifier = Modifier,
    onCancel : () -> Unit) {

    val (name, text) = when (replyTo) {
        is SendableWrap.MessageWrap -> replyTo.sender.name to replyTo.replyTitle
        is SendableWrap.PostWrap -> replyTo.channel?.name.orEmpty() to replyTo.replyTitle
        else -> null to null
    }

    if (name == null || text == null)
        return

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(REPLY_SIZE)
            .background(color = Colors.DarkGray),
        verticalAlignment = CenterVertically
    ) {

        Icon(
            Icons.Default.Close,
            contentDescription = "Cancel",
            tint = MaterialTheme.colors.surface,
            modifier = Modifier
                .padding(5.dp)
                .clip(CircleShape)
                .size(REPLY_SIZE - 15.dp)
                .clickable(onClick = onCancel)
        )

        Spacer(modifier = Modifier.width(5.dp))
        Spacer(modifier = Modifier
            .height(REPLY_SIZE - 10.dp)
            .width(1.dp)
            .background(MaterialTheme.colors.surface)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
        ) {

            Text(
                text = name,
                maxLines = 1,
                style = MaterialTheme.typography.body2,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = text,
                maxLines = 1,
                style = MaterialTheme.typography.body2,
                color = LocalTextStyle.current.color
            )
        }
    }
}


private val Sendable.replyTitle : String
    @Composable
    get() = when {
    this is TextSendable<*> && !text.isNullOrBlank() -> text!!
    this is VoiceSendable && voice != null -> {
        stringResource(id = R.string.voice)
    }
    this is TextSendable<*> && content?.isNotEmpty() == true ->
    with(content!!) {
        when {
            this.size == 1 && this[0].type == MediaContent.IMAGE ->
                stringResource(id = R.string.photo)
            this.all { it.type == MediaContent.IMAGE } ->
                stringResource(id = R.string.photos)
            this.size == 1 && this[0].type == MediaContent.VIDEO ->
                stringResource(id = R.string.video)
            this.all { it.type == MediaContent.VIDEO } ->
                stringResource(id = R.string.videos)
            this.size == 1 && this[0].type == MediaContent.GIF ->
                stringResource(id = R.string.gif)
            this.all { it.type == MediaContent.GIF } ->
                stringResource(id = R.string.gifs)
            else -> stringResource(id = R.string.file)
        }
    }
    else -> stringResource(id = R.string.message)
}