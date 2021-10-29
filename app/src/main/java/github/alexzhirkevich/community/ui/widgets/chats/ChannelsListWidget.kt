package github.alexzhirkevich.community.ui.widgets.chats

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import github.alexzhirkevich.community.core.entities.interfaces.Post
import github.alexzhirkevich.community.data.ChatWrap
import github.alexzhirkevich.community.data.DataState
import github.alexzhirkevich.community.ui.theme.Colors
import github.alexzhirkevich.community.data.viewmodels.ChannelsListViewModel
import github.alexzhirkevich.community.ui.theme.Dimens
import github.alexzhirkevich.community.ui.widgets.common.BackHandler
import github.alexzhirkevich.community.ui.widgets.common.verticalScrollbar
import github.alexzhirkevich.community.ui.widgets.screens.Routes
import github.alexzhirkevich.community.ui.widgets.screens.navigate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
@ExperimentalStdlibApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun ChannelsListWidget(
    navController: NavController,
    isSelectionEnabled: MutableState<Boolean>,
    channelsViewModel: ChannelsListViewModel = viewModel(),
    selectedItems: SnapshotStateList<String>,
    isFabVisible: MutableState<Boolean>,
    isLoadingInProgress : MutableState<Boolean>
) {

    val channels by channelsViewModel.data

    isLoadingInProgress.value =
        channels is DataState.Loading || channels is DataState.Cached

    BackHandler(enabled = isSelectionEnabled.value) {
        isSelectionEnabled.value = false
        selectedItems.clear()
    }


    when(channels) {
        is DataState.Success -> {
            SuccessChannelsWidget(
                channels = (channels as DataState.Success).value,
                navController = navController,
                isSelectionEnabled = isSelectionEnabled,
                selectedItems = selectedItems
            )
        }
    }
}

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalStdlibApi
@Composable
private fun SuccessChannelsWidget(
    channels : List<ChatWrap.ChannelWrap>,
    navController: NavController,
    isSelectionEnabled: MutableState<Boolean>,
    selectedItems: SnapshotStateList<String>,
) {

    val lazyListState = rememberLazyListState()

    if (channels.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier
                .background(color = Color.Transparent)
                .verticalScrollbar(lazyListState)
                .fillMaxSize(),
            state = lazyListState
        ) {

            items(count = channels.size,key = { channels[it].id }) {

                val channel = channels[it]

                val lastMessage = channel.lastMessage

                ChatWidget(
                    id = channel.id,
                    avatarUrl = channel.imageUri,
                    name = channel.name,
                    secondLine = when (lastMessage) {
                        is Post -> lastMessage.text.orEmpty().take(200)
                        else -> ""
                    },
                    time = System.currentTimeMillis(),
                    unreadCount = channel.unreadCount,
                    unreadBackground = Colors.Channels,
                    isSelectionEnabled = isSelectionEnabled,
                    selectedIds = selectedItems,
                    onClick = {
                        navController.navigate(Routes.ChannelScreen(channel.id))
                    },
                )
            }
        }
    } else{
        Box {
            Text(
                modifier = Modifier.align(Alignment.TopCenter),
                text = stringResource(id = R.string.dont_have_channels),
                fontSize = Dimens.FontSizeMedium
            )
        }
    }
}
