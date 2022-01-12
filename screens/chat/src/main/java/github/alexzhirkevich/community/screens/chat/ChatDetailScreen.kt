package github.alexzhirkevich.community.screens.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.NotificationsOff
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import github.alexzhirkevich.community.common.ChatWrap
import github.alexzhirkevich.community.common.composable.*
import github.alexzhirkevich.community.common.valueOrNull
import github.alexzhirkevich.community.routing.rememberTagNavigator
import github.alexzhirkevich.community.screens.chat.data.ChatDetailViewModel
import github.alexzhirkevich.community.theme.Colors
import github.alexzhirkevich.community.theme.Dimens
import github.alexzhirkevich.community.theme.Integers
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import me.onebone.toolbar.rememberCollapsingToolbarState

@ExperimentalAnimationApi
@Composable
fun ChatDetailScreen(
    navController: NavController,
    chatDetailViewModel : ChatDetailViewModel,
) {

    val chat by chatDetailViewModel.data.collectAsState()

    val tagNavigator = rememberTagNavigator(
        navController = navController,
        taggableRepository = chatDetailViewModel
    )

    val state = rememberCollapsingToolbarScaffoldState(
        toolbarState = rememberCollapsingToolbarState(initial = 0)
    )

    val (image,name,desc) = when(val c = chat.valueOrNull()){
        is ChatWrap.GroupWrap -> listOf(
            c.imageUri,
            c.name,
            stringResource(
                id = R.string.subscribers,
                c.membersCount
            ))
        else -> listOf("","","")
    }

    LaunchedEffect(key1 = null){
      //  state.toolbarState.collapse(0)
        state.toolbarState.expand(Integers.AnimDurationLong)
    }

    val scrollState = rememberScrollState()

    CollapsingToolbarScaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        state = state,
        scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
        toolbar = {
            CollapsingChatAppBar(
                toolbarState = state.toolbarState,
                scrollState = scrollState,
                imageUri = image,
                name = name,
                color = Colors.Chats,
                description = desc
            )
        },
        toolbarModifier = Modifier
            .background(Color.Transparent)
            .zIndex(1f)
    ) {



        val nChat = chat.valueOrNull()
        Box(Modifier.zIndex(2f)) {
            HorizontalShadowTop(modifier = Modifier
                .height(3.dp)
            )
            AnimatedVisibility(
                modifier = Modifier
                    .zIndex(3f)
                    .align(Alignment.TopEnd)
                    .absoluteOffset(x = -Dimens.FabMargin, y = -Dimens.FabSizeDefault / 2),
                visible = state.toolbarState.progress >= CollapseAvatarProgress,
                enter = scaleIn(),
                exit = scaleOut()
            ) {

                FloatingActionButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .size(Dimens.FabSizeDefault),
                    backgroundColor = MaterialTheme.colors.secondary,
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Email,
                        contentDescription = "Message",
                        tint = MaterialTheme.colors.surface
                    )
                }
            }
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .fillMaxSize()
              //      .zIndex(Float.MAX_VALUE)
            ) {
//            chat.valueOrNull()?.tag?.takeIf(String::isNotEmpty)?.let {
//                IconText(
//                    icon = Icons.Rounded.Tag,
//                    text = it,
//                    label = stringResource(id = R.string.tag),
//                )
//            }
//            chat.valueOrNull()?.description?.takeIf(String::isNotEmpty)?.let {
//                IconText(
//                    icon = Icons.Rounded.Info,
//                    text = it,
//                    label = stringResource(id = R.string.description),
//                    format = true,
//                    tagNavigator = tagNavigator
//                )
//            }

                chat.valueOrNull()?.isNotificationsEnabled?.let {

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
                    DetailMenuItemShimmer()
                }
                Text(text = buildString {
                    repeat(100) {
                        append("a\n")
                    }
                })
            }
        }
    }
}