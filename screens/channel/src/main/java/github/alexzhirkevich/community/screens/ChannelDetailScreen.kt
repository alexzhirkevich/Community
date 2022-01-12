package github.alexzhirkevich.community.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import github.alexzhirkevich.community.common.composable.CollapsingChatAppBar
import github.alexzhirkevich.community.common.composable.DetailMenuItem
import github.alexzhirkevich.community.common.valueOrNull
import github.alexzhirkevich.community.routing.rememberTagNavigator
import github.alexzhirkevich.community.screens.data.ChannelDetailViewModel
import me.onebone.toolbar.*

@Composable
fun ChannelDetailScreen(
    navController: NavController,
    channelDetailViewModel : ChannelDetailViewModel,
) {

    val channel by channelDetailViewModel.data.collectAsState()

    val tagNavigator = rememberTagNavigator(
        navController = navController,
        taggableRepository = channelDetailViewModel
    )

    val state = rememberCollapsingToolbarScaffoldState()
    val scrollState = rememberScrollState()

    CollapsingToolbarScaffold(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colors.background),
        state = state,
        scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
        toolbar = {
            val c = channel.valueOrNull()
            CollapsingChatAppBar(
                toolbarState = state.toolbarState,
                scrollState = scrollState,
                imageUri = c?.imageUri.orEmpty(),
                name = c?.name.orEmpty(),
                description = stringResource(
                    id = R.string.subscribers,
                    c?.membersCount?:0)
            )
        },
        toolbarModifier = Modifier.background(Color.Transparent)
    ) {


        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxSize()
                .zIndex(Float.MAX_VALUE)
        ) {
            channel.valueOrNull()?.tag?.takeIf(String::isNotEmpty)?.let {
                DetailMenuItem(
                    icon = Icons.Rounded.Tag,
                    text = it,
                    label = stringResource(id = R.string.tag),
                )
            }
            channel.valueOrNull()?.description?.takeIf(String::isNotEmpty)?.let {
                DetailMenuItem(
                    icon = Icons.Rounded.Info,
                    text = it,
                    label = stringResource(id = R.string.description),
                    format = true,
                    tagNavigator = tagNavigator
                )
            }

            channel.valueOrNull()?.isNotificationsEnabled?.let {

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .background(color = Color.Black)
                )

                DetailMenuItem(
                    icon = with(Icons.Rounded) {
                        if (it) Notifications else NotificationsOff
                    },
                    text = stringResource(id = R.string.notifications),
                    label = stringResource(
                        id =
                        if (it) R.string.notifications_enabled
                        else R.string.notifications_disabled
                    )
                )
            }
        }
    }
}
