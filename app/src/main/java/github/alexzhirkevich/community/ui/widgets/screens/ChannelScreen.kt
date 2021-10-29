package github.alexzhirkevich.community.ui.widgets.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import github.alexzhirkevich.community.R
import github.alexzhirkevich.community.data.DataState
import github.alexzhirkevich.community.data.viewmodels.ChannelViewModel
import github.alexzhirkevich.community.ui.theme.Colors
import github.alexzhirkevich.community.ui.widgets.chats.ChatAppBar
import github.alexzhirkevich.community.ui.widgets.common.MessageInput
import github.alexzhirkevich.community.ui.widgets.posts.PostsListWidget

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalStdlibApi
@Composable
fun ChannelScreen(
    navHostController: NavHostController,
    id: String,
    viewModel : ChannelViewModel = viewModel(
        key = id,
        factory = ChannelViewModel.Factory(id)
    )
){

    val channel = viewModel.data.value

    val isSelectionEnabled = remember {
        mutableStateOf(false)
    }

    val selectedItems = remember {
        mutableStateListOf<String>()
    }

    Scaffold(
        topBar = {
            val (name, description) = when (channel) {
                is DataState.Success ->
                    channel.value.name to stringResource(
                        id = R.string.subscribers,
                        channel.value.membersCount
                    )
                else -> "" to ""
            }
            ChatAppBar(
                navController = navHostController,
                name = name,
                description = description,
                color = Colors.Channels
            )
        }
    ){
        Column {
            Box(
                modifier = Modifier.weight(1f)
            ) {
                PostsListWidget(
                    navController = navHostController,
                    channelViewModel = viewModel,
                    isSelectionEnabled = isSelectionEnabled,
                    selectedItems = selectedItems,
                )
            }

            if (channel is DataState.Success && channel.value.isAdmin!=null)
                MessageInput()
        }
    }
}
