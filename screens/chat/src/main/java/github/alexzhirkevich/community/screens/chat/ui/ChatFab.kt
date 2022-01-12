package github.alexzhirkevich.community.screens.chat.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import github.alexzhirkevich.community.common.ChatWrap
import github.alexzhirkevich.community.common.DataState
import github.alexzhirkevich.community.common.composable.isLastItemVisible
import github.alexzhirkevich.community.theme.Colors
import github.alexzhirkevich.community.theme.Dimens
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@Composable
fun ChatFab(
    channel : DataState<ChatWrap>,
    listState: LazyListState,
    modifier : Modifier = Modifier,
    ){
    val unreadFontSize = MaterialTheme.typography.body1.fontSize

    val scope = rememberCoroutineScope()

    AnimatedVisibility(
        visible = !listState.isLastItemVisible(),
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier
            .padding(Dimens.FabMargin)
    ) {
        Box() {
            if (channel is DataState.Success) {
                Text(
                    text = channel.value.unreadCount.toString(),
                    fontSize = unreadFontSize,
                    color = Colors.DarkGray,
                    modifier = Modifier
                        .background(MaterialTheme.colors.secondaryVariant,
                            shape = CircleShape)
                        .padding(3.dp)
                        .zIndex(2f)
                        .align(Alignment.TopCenter)
                )
            }

            val density = LocalDensity.current
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Spacer(modifier = Modifier.height(
                    with(density){
                        unreadFontSize.toDp()
                    }
                ))
                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            val last = listState.layoutInfo.totalItemsCount - 1
                            if (last>=0)
                                listState.scrollToItem(last)
                        }
                    },
                    backgroundColor = Colors.DarkGray,
                    modifier = Modifier
                        .size(Dimens.FabSizeDefault)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowDownward,
                        contentDescription = "Move Down",
                        tint = MaterialTheme.colors.secondaryVariant
                    )
                }
            }
        }
    }
}