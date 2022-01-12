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
import github.alexzhirkevich.community.bottomnavigation.chats.data.ChannelsViewModel
import github.alexzhirkevich.community.common.ChatWrap
import github.alexzhirkevich.community.common.DataState
import github.alexzhirkevich.community.common.composable.SelectionState
import github.alexzhirkevich.community.common.composable.verticalScrollbar
import github.alexzhirkevich.community.core.entities.MediaContent
import github.alexzhirkevich.community.core.entities.interfaces.Post
import github.alexzhirkevich.community.routing.Route
import github.alexzhirkevich.community.routing.navigate
import github.alexzhirkevich.community.theme.Colors
import github.alexzhirkevich.community.theme.Dimens
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
@ExperimentalStdlibApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
internal fun ChannelsListWidget(
    navController: NavController,
    channelsViewModel: ChannelsViewModel = viewModel(),
    selectionState: SelectionState,
    isFabVisible: MutableState<Boolean>,
    isLoadingInProgress : MutableState<Boolean>
) {

    val channels by channelsViewModel.data.collectAsState()

    isLoadingInProgress.value =
        channels is DataState.Loading || channels is DataState.Cached


    when(channels) {
        is DataState.Success -> {
            SuccessChannelsWidget(
                channels = (channels as DataState.Success).value,
                navController = navController,
                selectionState = selectionState
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
    navController : NavController,
    selectionState : SelectionState,
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

            items(count = channels.size, key = { channels[it].id }) {

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
                    selectionState = selectionState,
                    onClick = {
                        navController.navigate(Route.ChannelScreen(channel.id))
                    },
                )
            }
        }
    } else{
        Box {
            Text(
                modifier = Modifier.align(Alignment.TopCenter),
                text = stringResource(id = R.string.dont_have_channels),
                style = MaterialTheme.typography.body1,
            )
        }
    }
}
