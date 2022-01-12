package github.alexzhirkevich.community.bottomnavigation.chats.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import github.alexzhirkevich.community.bottomnavigation.R
import github.alexzhirkevich.community.bottomnavigation.chats.data.ChatsViewModel
import github.alexzhirkevich.community.common.ChatWrap
import github.alexzhirkevich.community.common.DataState
import github.alexzhirkevich.community.common.composable.SelectionState
import github.alexzhirkevich.community.common.composable.verticalScrollbar
import github.alexzhirkevich.community.core.entities.interfaces.Message
import github.alexzhirkevich.community.routing.Route
import github.alexzhirkevich.community.routing.navigate
import github.alexzhirkevich.community.theme.Colors
import github.alexzhirkevich.community.theme.Dimens

@ExperimentalStdlibApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
internal fun ChatsListWidget(
    navController: NavController,
    chatsViewModel: ChatsViewModel = viewModel(),
    isFabVisible: MutableState<Boolean>,
    selectionState: SelectionState,
    isLoadingInProgress : MutableState<Boolean>
){

    val chats by chatsViewModel.data.collectAsState()

    isLoadingInProgress.value =
        chats is DataState.Loading || chats is DataState.Cached


    when (chats){


        is DataState.Success -> SuccessChatsWidget(
            chats = (chats as DataState.Success).value,
            navController = navController,
            selectionState = selectionState,
            isFabVisible = isFabVisible,
            )
    }
}

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
private fun SuccessChatsWidget(
    chats: List<ChatWrap>,
    navController: NavController,
    selectionState: SelectionState,
    isFabVisible: MutableState<Boolean>,
){

    val lazyListState = rememberLazyListState()


    if (chats.isNotEmpty()) {
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .background(color = Color.Transparent)
                .verticalScrollbar(lazyListState)
                .fillMaxSize(),
            ) {

            items(count = chats.size, key = {chats[it].id}) {

                val chat = chats[it]
                val lastMessage = chat.lastMessage

                val (name, imageUri) = when (chat) {
                    is ChatWrap.DialogWrap -> chat.companion.name to chat.companion.imageUri
                    is ChatWrap.GroupWrap -> chat.name to chat.imageUri
                    else -> "" to ""
                }

                ChatWidget(
                    id = chat.id,
                    avatarUrl = imageUri,
                    name = name,
                    secondLine = when (lastMessage) {
                        is Message -> lastMessage.text.orEmpty()
                        else -> "Last message"
                    },
                    time = System.currentTimeMillis(),
                    unreadCount = chat.unreadCount,
                    unreadBackground = Colors.Chats,
                    selectionState = selectionState
                ) {
                    navController.navigate(Route.ChatScreen(chat.id))
                }
            }
        }
    } else {
        Box {
            Text(
                modifier = Modifier.align(Alignment.TopCenter),
                text = stringResource(id = R.string.dont_have_chats),
                style = MaterialTheme.typography.body1,
            )
        }
    }
}