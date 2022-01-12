package github.alexzhirkevich.community.screens.chat.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import github.alexzhirkevich.community.common.BuildConfig
import github.alexzhirkevich.community.common.DataState
import github.alexzhirkevich.community.common.SendableWrap
import github.alexzhirkevich.community.common.collectAsState
import github.alexzhirkevich.community.common.composable.scrollOnResize
import github.alexzhirkevich.community.common.composable.verticalScrollbar
import github.alexzhirkevich.community.common.util.dateTime
import github.alexzhirkevich.community.core.entities.interfaces.Group
import github.alexzhirkevich.community.features.stickyheader.TextStickyHeader
import github.alexzhirkevich.community.features.stickyheader.rememberHeaderProperties
import github.alexzhirkevich.community.routing.rememberTagNavigator
import github.alexzhirkevich.community.screens.chat.R
import github.alexzhirkevich.community.screens.chat.data.ChatViewModel
import github.alexzhirkevich.community.theme.Dimens
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalComposeUiApi
@FlowPreview
@ExperimentalCoroutinesApi
@ExperimentalStdlibApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
internal fun MessagesListWidget(
    navController: NavController,
    viewModel: ChatViewModel,
    isSelectionEnabled: MutableState<Boolean>,
    selectedItems: SnapshotStateList<String>,
    lazyListState: LazyListState,
    modifier: Modifier = Modifier,
    replyTo: MutableState<SendableWrap.MessageWrap?>,
    ){

    val messages by viewModel.messages
        .collectAsState()

    val chat by viewModel.data.collectAsState()

    BackHandler(enabled = isSelectionEnabled.value) {
        isSelectionEnabled.value = false
        selectedItems.clear()
    }

    when {
        messages is DataState.Success &&
                chat is DataState.Success<*> ->
            SuccessMessagesWidget(
                modifier = Modifier,
                messages = (messages as DataState.Success).value.toList(),
                navController = navController,
                isSelectionEnabled = isSelectionEnabled,
                selectedItems = selectedItems,
                lazyListState = lazyListState,
                viewModel = viewModel,
                replyTo = replyTo
            )

    }
}

@ExperimentalComposeUiApi
@ExperimentalCoroutinesApi
@FlowPreview
@ExperimentalStdlibApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
private fun SuccessMessagesWidget(
    messages: List<SendableWrap>,
    navController: NavController,
    isSelectionEnabled: MutableState<Boolean>,
    selectedItems: SnapshotStateList<String>,
    lazyListState: LazyListState,
    viewModel: ChatViewModel,
    modifier: Modifier = Modifier,
    replyTo: MutableState<SendableWrap.MessageWrap?>,
) {

    val context = LocalContext.current

    val headerProps = rememberHeaderProperties(
        lazyListState = lazyListState,
        allItems = messages) {
        it.time.dateTime(context).dayAndMonthLocalized
    }

    val firstNewMessage by viewModel.firstNewMessage
        .collectAsState()

    val tagNavigator = rememberTagNavigator(
        navController = navController,
        taggableRepository = viewModel)

    LazyColumn(
        state = lazyListState,
        modifier = modifier
            .background(color = Color.Transparent)
            .verticalScrollbar(lazyListState)
            .fillMaxSize()
            .scrollOnResize(lazyListState),
        verticalArrangement = Arrangement.Bottom

    ) {

        messages.groupBy {
            it.time.dateTime(context).dayAndMonthLocalized
        }.forEach { groupedMessages ->

            stickyHeader(key = groupedMessages.key) {
                TextStickyHeader(
                    text = groupedMessages.key,
                    lazyListState = lazyListState,
                    fontSize = MaterialTheme.typography.body1.fontSize,
                    textColor = LocalTextStyle.current.color,
                    fadeout = headerProps.isSticky(groupedMessages.key),
                    backgroundColor = MaterialTheme.colors.background,
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .alpha(.9f)
                )
            }

            items(
                count = groupedMessages.value.size,
                key = { groupedMessages.value[it].id }) {


                when (val message = groupedMessages.value[it]) {
                    is SendableWrap.MessageWrap -> {


                        val isFirstNew = (firstNewMessage as? DataState.Success)
                            ?.value?.id == message.id
                        if (isFirstNew) {
                            Box(
                                modifier = Modifier
                                    .padding(vertical = 10.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    modifier = Modifier
                                        .align(Alignment.Center),
                                    text = stringResource(id = R.string.new_messages),
                                    fontSize = MaterialTheme.typography.body1.fontSize,
                                    color = MaterialTheme.colors.secondaryVariant
                                )
                            }
                        }

                        val index = messages.indexOf(message)
                        MessageWidget(
                            isGroup = (viewModel.data.value as? DataState.Success)?.value is Group,
                            message = message,
                            viewModel = viewModel,
                            isSelectionEnabled = isSelectionEnabled,
                            selectedItems = selectedItems,
                            tagNavigator = tagNavigator,
                            previous = if (!isFirstNew && it != 0 && index - 1 in messages.indices)
                                messages[index - 1] else null,
                            next = if (it != groupedMessages.value.size-1 && index + 1 in messages.indices)
                                messages[index + 1] else null,
                            replyTo = replyTo,
                            modifier = modifier.padding(horizontal = 10.dp)
                        )
                    }

                    is SendableWrap.SystemMessageWrap ->
                        SystemMessageWidget(message = message)

                    is SendableWrap.PostWrap ->
                        if (BuildConfig.DEBUG)
                            error("Trying to draw post as a message")
                }
            }
        }
    }
}