package github.alexzhirkevich.community.ui.widgets.posts

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import github.alexzhirkevich.community.BuildConfig
import github.alexzhirkevich.community.common.util.dateTime
import github.alexzhirkevich.community.data.ChatWrap
import github.alexzhirkevich.community.data.DataState
import github.alexzhirkevich.community.data.SendableWrap
import github.alexzhirkevich.community.data.viewmodels.ChannelViewModel
import github.alexzhirkevich.community.data.viewmodels.collectAsState
import github.alexzhirkevich.community.ui.theme.Colors
import github.alexzhirkevich.community.ui.theme.Dimens
import github.alexzhirkevich.community.ui.widgets.common.BackHandler
import github.alexzhirkevich.community.ui.widgets.common.DateStickyHeader
import github.alexzhirkevich.community.ui.widgets.common.verticalScrollbar

@ExperimentalStdlibApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun PostsListWidget(
    navController: NavController,
    channelViewModel: ChannelViewModel,
    isSelectionEnabled: MutableState<Boolean>,
    selectedItems: SnapshotStateList<String>,
){

    val posts by channelViewModel.posts
        .collectAsState()

    BackHandler(enabled = isSelectionEnabled.value) {
        isSelectionEnabled.value = false
        selectedItems.clear()
    }

    when {
        posts is DataState.Success &&
                channelViewModel.data.value is DataState.Success<*> ->
            SuccessPostsWidget(
                channel = (channelViewModel.data.value as DataState.Success).value,
                posts = (posts as DataState.Success).value.toList(),
                navController = navController,
                isSelectionEnabled = isSelectionEnabled,
                selectedItems = selectedItems,
            )

    }
}

@ExperimentalStdlibApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
private fun SuccessPostsWidget(
    channel : ChatWrap.ChannelWrap,
    posts: List<SendableWrap>,
    navController: NavController,
    isSelectionEnabled: MutableState<Boolean>,
    selectedItems: SnapshotStateList<String>,
) {

    val lazyListState = rememberLazyListState()
    val context = LocalContext.current


    LazyColumn(
        state = lazyListState,
        modifier = Modifier
            .background(color = Color.Transparent)
            .verticalScrollbar(lazyListState)
            .fillMaxSize(),
    ) {

        posts.groupBy {
            it.time.dateTime(context).dayAndMonthLocalized
        }.forEach { groupedPosts ->

            stickyHeader(key = groupedPosts.key) {

                val firstIdx = lazyListState.firstVisibleItemIndex.let {
                    if (it >0) it-1 else it
                }

                val density = LocalDensity.current

                DateStickyHeader(
                    time = groupedPosts.key,
                    lazyListState = lazyListState,
                    fadeout = posts[firstIdx] in groupedPosts.value && firstIdx != 0 ||
                            firstIdx ==0 &&
                            lazyListState.firstVisibleItemScrollOffset >=
                            with(density){
                                Dimens.FontSizeMedium.roundToPx() + 15.dp.roundToPx()
                            }
                )
            }

            items(count = groupedPosts.value.size, key = { groupedPosts.value[it].id }) {

                val post = groupedPosts.value[it]

                when (post) {
                    is SendableWrap.PostWrap ->
                        PostWidget(channel = channel, post = post)
                    is SendableWrap.SystemMessageWrap ->
                        PostWidget(post = post)
                    else ->
                        if (BuildConfig.DEBUG)
                            error("Trying to draw message as a post")
                }
            }
        }
    }
}