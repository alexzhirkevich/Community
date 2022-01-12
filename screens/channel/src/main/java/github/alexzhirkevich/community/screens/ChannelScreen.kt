package github.alexzhirkevich.community.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.InternalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import github.alexzhirkevich.community.common.DataState
import github.alexzhirkevich.community.common.SendableWrap
import github.alexzhirkevich.community.common.composable.ChatAppBar
import github.alexzhirkevich.community.common.valueOrNull
import github.alexzhirkevich.community.routing.Route
import github.alexzhirkevich.community.routing.navigate
import github.alexzhirkevich.community.screens.chat.ui.ChatFab
import github.alexzhirkevich.community.screens.data.ChannelViewModel
import github.alexzhirkevich.community.screens.ui.PostsListWidget
import github.alexzhirkevich.community.theme.Colors
import github.alexzhirkevich.messageinput.MessageInput
import github.alexzhirkevich.messageinput.PostInput
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.plus

@InternalAnimationApi
@ExperimentalCoroutinesApi
@FlowPreview
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalStdlibApi
@Composable
fun ChannelScreen(
    navHostController: NavController,
    viewModel: ChannelViewModel,
) {

    val channel by viewModel.data.collectAsState()

    val isSelectionEnabled = remember {
        mutableStateOf(false)
    }

    val selectedItems = remember {
        mutableStateListOf<String>()
    }

    val haptic = LocalHapticFeedback.current

    LaunchedEffect(key1 = isSelectionEnabled.value) {
        if (isSelectionEnabled.value) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

    val lazyListState = rememberLazyListState()


    Scaffold(
        topBar = {
            val (name, description) = when (val c = channel) {
                is DataState.Success ->
                    c.value.name to stringResource(
                        id = R.string.subscribers,
                        c.value.membersCount
                    )
                else -> "" to ""
            }
            ChatAppBar(
                name = name,
                description = description,
                color = Colors.Channels,
            ){
                channel.valueOrNull()?.id?.let {
                    navHostController.navigate(
                        Route.ChannelDetailScreen(it)
                    )
                }
            }
        }
    ) {

        val replyTo = remember {
            mutableStateOf<SendableWrap.PostWrap?>(null)
        }

        PostInput(
            builder = viewModel,
            storageRepository = viewModel,
            coroutineScope = viewModel.viewModelScope + Dispatchers.IO,
            visible = channel.valueOrNull()?.isAdmin != null,
            modifier = Modifier.fillMaxSize(),
            onSend = {
                viewModel.createPost(it)
            },
            replyTo = replyTo
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                PostsListWidget(
                    navController = navHostController,
                    channelViewModel = viewModel,
                    isSelectionEnabled = isSelectionEnabled,
                    selectedItems = selectedItems,
                    lazyListState = lazyListState
                )
                ChatFab(
                    channel = channel,
                    listState = lazyListState,
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }
        }
    }
}
//@ExperimentalAnimationApi
//@Composable
//private fun Fab(
//    channel : DataState<ChatWrap>,
//    postsListState: LazyListState,
//    modifier : Modifier = Modifier,
//
//){
//    val unreadFontSize = Dimens.FontSizeMedium
//
//    val scope = rememberCoroutineScope()
//
//    AnimatedVisibility(
//        visible = !postsListState.isLastItemVisible(),
//        enter = fadeIn(),
//        exit = fadeOut(),
//        modifier = modifier
//            .padding(Dimens.FabMargin)
//    ) {
//        Box() {
//            if (channel is DataState.Success) {
//                Text(
//                    text = channel.value.unreadCount.toString(),
//                    fontSize = unreadFontSize,
//                    color = Colors.DarkGray,
//                    modifier = Modifier
//                        .background(Colors.Gray, shape = CircleShape)
//                        .padding(3.dp)
//                        .zIndex(2f)
//                        .align(Alignment.TopCenter)
//                )
//            }
//
//            val density = LocalDensity.current
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally
//            ){
//                Spacer(modifier = Modifier.height(
//                    with(density){
//                        unreadFontSize.toDp()
//                    }
//                ))
//                FloatingActionButton(
//                    onClick = {
//                        scope.launch {
//                            postsListState.scrollToItem(postsListState.layoutInfo.totalItemsCount - 1)
//                        }
//                    },
//                    backgroundColor = Colors.DarkGray,
//                    modifier = Modifier
//                        .size(Dimens.FabSizeDefault)
//                ) {
//                    Icon(
//                        imageVector = Icons.Rounded.ArrowDownward,
//                        contentDescription = "Move Down",
//                        tint = Colors.Gray
//                    )
//                }
//            }
//        }
//    }
//}