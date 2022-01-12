package github.alexzhirkevich.community.screens.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import github.alexzhirkevich.community.common.ChatWrap
import github.alexzhirkevich.community.common.DataState
import github.alexzhirkevich.community.common.SendableWrap
import github.alexzhirkevich.community.common.collectAsState
import github.alexzhirkevich.community.common.composable.scrollOnResize
import github.alexzhirkevich.community.common.composable.verticalScrollbar
import github.alexzhirkevich.community.common.util.dateTime
import github.alexzhirkevich.community.core.repo.interfaces.TaggableRepository
import github.alexzhirkevich.community.features.stickyheader.TextStickyHeader
import github.alexzhirkevich.community.features.stickyheader.rememberHeaderProperties
import github.alexzhirkevich.community.routing.rememberTagNavigator
import github.alexzhirkevich.community.screens.BuildConfig
import github.alexzhirkevich.community.screens.chat.ui.SystemMessageWidget
import github.alexzhirkevich.community.screens.data.ChannelViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
@ExperimentalStdlibApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
internal fun PostsListWidget(
    navController: NavController,
    channelViewModel: ChannelViewModel,
    isSelectionEnabled: MutableState<Boolean>,
    selectedItems: SnapshotStateList<String>,
    lazyListState: LazyListState,
){

    val posts by channelViewModel.posts
        .collectAsState()

    val channel by channelViewModel.data.collectAsState()

    BackHandler(enabled = isSelectionEnabled.value) {
        isSelectionEnabled.value = false
        selectedItems.clear()
    }

    when {
        posts is DataState.Success &&
                channel is DataState.Success<*> ->
            SuccessPostsWidget(
                channel = (channel as DataState.Success).value,
                posts = (posts as DataState.Success).value.toList(),
                navController = navController,
                isSelectionEnabled = isSelectionEnabled,
                selectedItems = selectedItems,
                lazyListState = lazyListState,
                taggableRepository = channelViewModel
            )

    }
}

@ExperimentalCoroutinesApi
@FlowPreview
@ExperimentalStdlibApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
private fun SuccessPostsWidget(
    channel: ChatWrap.ChannelWrap,
    posts: List<SendableWrap>,
    navController: NavController,
    isSelectionEnabled: MutableState<Boolean>,
    selectedItems: SnapshotStateList<String>,
    lazyListState: LazyListState,
    taggableRepository: TaggableRepository
) {

    val context = LocalContext.current

    val tagNavigator = rememberTagNavigator(
        navController = navController,
        taggableRepository = taggableRepository)

    val headerProps = rememberHeaderProperties(
        lazyListState = lazyListState,
        allItems = posts) {
            it.time.dateTime(context).dayAndMonthLocalized
        }

    LazyColumn(
        state = lazyListState,
        modifier = Modifier
            .background(color = Color.Transparent)
            .verticalScrollbar(lazyListState)
            .fillMaxSize()
            .scrollOnResize(lazyListState),
        verticalArrangement = Arrangement.Bottom
    ) {

        posts.groupBy {
            it.time.dateTime(context).dayAndMonthLocalized
        }.forEach { groupedPosts ->

            stickyHeader(key = groupedPosts.key) {

                TextStickyHeader(
                    text = groupedPosts.key,
                    lazyListState = lazyListState,
                    textColor = LocalTextStyle.current.color,
                    fadeout = headerProps.isSticky(groupedPosts.key),
                    backgroundColor = MaterialTheme.colors.primaryVariant,
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .alpha(.8f)
                )
            }

            items(count = groupedPosts.value.size, key = { groupedPosts.value[it].id }) {

                when (val post = groupedPosts.value[it]) {
                    is SendableWrap.PostWrap ->
                        PostWidget(
                            channel = channel,
                            post = post,
                            isSelectionEnabled = isSelectionEnabled,
                            selectedItems = selectedItems,
                            tagNavigator = tagNavigator
                        )

                    is SendableWrap.SystemMessageWrap ->
                        SystemMessageWidget(message = post)

                    is SendableWrap.MessageWrap ->
                        if (BuildConfig.DEBUG)
                            error("Trying to draw message as a post")
                }
            }
        }
    }
}