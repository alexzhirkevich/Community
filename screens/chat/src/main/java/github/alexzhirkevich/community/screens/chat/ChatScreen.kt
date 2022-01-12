package github.alexzhirkevich.community.screens.chat

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import github.alexzhirkevich.community.common.ChatWrap
import github.alexzhirkevich.community.common.DataState
import github.alexzhirkevich.community.common.SendableWrap
import github.alexzhirkevich.community.common.composable.ChatAppBar
import github.alexzhirkevich.community.common.util.dateTime
import github.alexzhirkevich.community.common.valueOrNull
import github.alexzhirkevich.community.routing.Route
import github.alexzhirkevich.community.routing.navigate
import github.alexzhirkevich.community.screens.chat.data.ChatViewModel
import github.alexzhirkevich.community.screens.chat.ui.ChatFab
import github.alexzhirkevich.community.screens.chat.ui.MessagesListWidget
import github.alexzhirkevich.community.theme.Colors
import github.alexzhirkevich.messageinput.MessageInput
import kotlinx.coroutines.*

@ExperimentalComposeUiApi
@ExperimentalCoroutinesApi
@FlowPreview
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalStdlibApi
@Composable
fun ChatScreen(
    navHostController: NavController,
    viewModel : ChatViewModel
) {

    LaunchedEffect(null){
        viewModel.update()
    }

    DisposableEffect(null){
        onDispose {
            viewModel.release()
        }
    }

    val chat by viewModel.data.collectAsState()

    val isSelectionEnabled = remember {
        mutableStateOf(false)
    }

    val selectedItems = remember {
        mutableStateListOf<String>()
    }

    val lazyListState = rememberLazyListState()

    val scope = rememberCoroutineScope()

    val replyTo = remember {
        mutableStateOf<SendableWrap.MessageWrap?>(null)
    }

    Scaffold(
        topBar = {
            val (name, image, description) = when (chat) {
                is DataState.Success ->
                    when (val c = chat.valueOrNull()) {
                        is ChatWrap.GroupWrap ->
                            listOf(c.name,
                                c.imageUri,
                                stringResource(
                                    id = R.string.members,
                                    c.membersCount
                                )
                            )
                        is ChatWrap.DialogWrap ->
                            listOf(
                                c.companion.name,
                                c.companion.imageUri,
                                if (c.companion.isOnline)
                                    stringResource(
                                        id = R.string.online,
                                    )
                                else
                                    c.companion.lastOnline.dateTime()
                                        .lastOnline
                            )
                        else -> listOf("","","")

                    }
                else -> listOf("","","")
            }
            ChatAppBar(
                name = name,
                imageUri = image,
                description = description,
                color = Colors.Chats
            ){
                when (val c = chat.valueOrNull()){
                    is ChatWrap.GroupWrap -> {
                        navHostController.navigate(Route.GroupDetailScreen(c.id))
                    }
                }
            }
        }
    ) {

        MessageInput(
            builder = viewModel,
            storageRepository = viewModel,
            coroutineScope = viewModel.viewModelScope + Dispatchers.IO,
            replyTo = replyTo,
            onSend = {
                viewModel.sendMessage(it){
                    delay(100)
                    scope.launch {
                        viewModel.messages.value.valueOrNull()?.size?.let { it1 ->
                            val last = lazyListState.layoutInfo.totalItemsCount - 1
                            if (last >= 0)
                                lazyListState.scrollToItem(last)
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                MessagesListWidget(
                    navController = navHostController,
                    viewModel = viewModel,
                    isSelectionEnabled = isSelectionEnabled,
                    selectedItems = selectedItems,
                    lazyListState = lazyListState,
                    replyTo = replyTo
                )
                ChatFab(
                    channel = chat,
                    listState = lazyListState,
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }
        }
    }
}

