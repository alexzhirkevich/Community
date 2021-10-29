package github.alexzhirkevich.community.ui.widgets.chats

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import github.alexzhirkevich.community.R
import github.alexzhirkevich.community.core.entities.interfaces.Message
import github.alexzhirkevich.community.data.ChatWrap
import github.alexzhirkevich.community.data.DataState
import github.alexzhirkevich.community.ui.theme.Colors
import github.alexzhirkevich.community.data.viewmodels.ChatsViewModel
import github.alexzhirkevich.community.ui.theme.Dimens
import github.alexzhirkevich.community.ui.widgets.common.BackHandler
import github.alexzhirkevich.community.ui.widgets.common.verticalScrollbar

@ExperimentalStdlibApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun ChatsListWidget(
    navController: NavController,
    isSelectionEnabled: MutableState<Boolean>,
    chatsViewModel: ChatsViewModel = viewModel(),
    selectedItems: SnapshotStateList<String>,
    isFabVisible: MutableState<Boolean>,
    isLoadingInProgress : MutableState<Boolean>
){

    val chats by chatsViewModel.data

    isLoadingInProgress.value =
        chats is DataState.Loading || chats is DataState.Cached

    BackHandler(enabled = isSelectionEnabled.value) {
        isSelectionEnabled.value = false
        selectedItems.clear()
    }

    when (chats){


        is DataState.Success -> SuccessChatsWidget(
            chats = (chats as DataState.Success).value,
            navController = navController,
            isSelectionEnabled = isSelectionEnabled,
            selectedItems = selectedItems,
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
    isSelectionEnabled: MutableState<Boolean>,
    selectedItems: SnapshotStateList<String>,
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
                    isSelectionEnabled = isSelectionEnabled,
                    selectedIds = selectedItems,
                ) {
                }
            }
        }
    } else {
        Box {
            Text(
                modifier = Modifier.align(Alignment.TopCenter),
                text = stringResource(id = R.string.dont_have_chats),
                fontSize = Dimens.FontSizeMedium
            )
        }
    }
}