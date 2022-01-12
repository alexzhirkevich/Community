package github.alexzhirkevich.community.bottomnavigation.chats

import android.animation.ValueAnimator
import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.MarkEmailRead
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.pager.*
import github.alexzhirkevich.community.bottomnavigation.R
import github.alexzhirkevich.community.bottomnavigation.chats.data.ChannelsViewModel
import github.alexzhirkevich.community.bottomnavigation.chats.data.ChatsViewModel
import github.alexzhirkevich.community.bottomnavigation.chats.ui.ChannelsListWidget
import github.alexzhirkevich.community.bottomnavigation.chats.ui.ChatsListWidget
import github.alexzhirkevich.community.common.composable.HorizontalShadowBottom
import github.alexzhirkevich.community.common.composable.HorizontalShadowTop
import github.alexzhirkevich.community.common.composable.SelectionState
import github.alexzhirkevich.community.common.composable.rememberSelectionState
import github.alexzhirkevich.community.theme.*
import github.alexzhirkevich.community.theme.Colors
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@ExperimentalCoroutinesApi
@FlowPreview
@ExperimentalStdlibApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
internal fun ChatsViewPagerScreen(
    navHostController: NavController,
    chatsViewModel : ChatsViewModel = hiltViewModel(),
    channelsViewModel : ChannelsViewModel = hiltViewModel()
) {

    val pagerState = rememberPagerState()

    val chatSelectionState = chatsViewModel.selectionState
    val channelSelectionState = channelsViewModel.selectionState

    val currentSelectionState = if (pagerState.currentPage == 0)
        chatSelectionState else channelSelectionState

    val isFabVisible = rememberSaveable {
        mutableStateOf(true)
    }

    val isLoadingInProgress = rememberSaveable {
        mutableStateOf(false)
    }


    val haptic = LocalHapticFeedback.current
    LaunchedEffect(key1 = chatSelectionState.isEnabled.value){
        if (chatSelectionState.isEnabled.value){
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }
    LaunchedEffect(key1 = channelSelectionState.isEnabled.value){
        if (channelSelectionState.isEnabled.value){
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }
    BackHandler(enabled = currentSelectionState.isEnabled.value) {
        currentSelectionState.setEnabled(false)
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        Column {
            AppBar(
                pagerState = pagerState,
                selectionState = currentSelectionState,
                isLoadingInProgress = isLoadingInProgress.value
            )
            Pager(
                navController = navHostController,
                pagerState = pagerState,
                chatsViewModel = chatsViewModel,
                channelsViewModel = channelsViewModel,
                isFabVisible = isFabVisible,
                isLoadingInProgress = isLoadingInProgress,
            )
        }
    }
    AllFabs(
        pagerState = pagerState,
        isFabVisible = isFabVisible
    )

}

@ExperimentalPagerApi
@ExperimentalAnimationApi
@Composable
private fun AppBar(
    pagerState: PagerState,
    selectionState: SelectionState,
    isLoadingInProgress: Boolean,
) {
    TopAppBar(
        modifier = Modifier.padding(end = 10.dp),
        title = {},
        backgroundColor = Color.Transparent,
        contentColor = Color.Gray,
        elevation = 0.dp,

        actions = {

            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxWidth(),
                visible = !selectionState.isEnabled.value,
                enter = slideInVertically {
                    -it
                },
                exit = slideOutVertically{ -it }
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppbarIcon(
                        modifier = Modifier,
                        icon = Icons.Filled.Search,
                        contentDescription = "Search"
                    ) {
                    }
                    Tabs(pagerState = pagerState)

                    Spacer(modifier = Modifier.width(10.dp))

                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(Dimens.VPIndicatorHeight)
                            .alpha(if (isLoadingInProgress) 1f else 0f),
                        color = LocalTextStyle.current.color,
                        strokeWidth = Dimens.CircleProgressStrokeWidth
                    )

                    Spacer(modifier = Modifier.width(10.dp))
                }
            }

            AnimatedVisibility(
                visible = selectionState.isEnabled.value,
                enter = slideInVertically{ h -> -h },
                exit = slideOutVertically{ h -> -h }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AppbarIcon(
                            icon = Icons.Rounded.Close,
                            contentDescription = "Close"
                        ) {
                           selectionState.setEnabled(false)
                        }
                        AnimatedContent(
                            targetState = selectionState.selectedItems.size,
                            transitionSpec = {
                                if (targetState > initialState) {

                                    slideInVertically{ it } + fadeIn() with
                                            slideOutVertically{ -it } + fadeOut()
                                } else {

                                    slideInVertically{ -it } + fadeIn() with
                                            slideOutVertically{ it } + fadeOut()
                                }.using(
                                    SizeTransform(clip = false)
                                )
                            }
                        ) { tc ->
                            Text(text = tc.toString(),fontSize = MaterialTheme.typography.body1.fontSize,
                            )
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {

                        AppbarIcon(
                            icon = Icons.Outlined.MarkEmailRead,
                            contentDescription = "Read"
                        ) {

                        }

                        AppbarIcon(
                            icon = Icons.Outlined.Delete,
                            contentDescription = "Delete"
                        ) {

                        }
                        AppbarIcon(
                            icon = Icons.Outlined.MoreVert,
                            contentDescription = "Options"
                        ) {
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun AppbarIcon(
    modifier: Modifier = Modifier,
    icon : ImageVector,
    contentDescription : String,
    onClick: () -> Unit,
){
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            imageVector = icon,
            tint = MaterialTheme.colors.surface,
            contentDescription = contentDescription
        )
    }
}

@ExperimentalPagerApi
@Composable
private fun RowScope.Tabs(pagerState: PagerState) {

    val coroutineScope = rememberCoroutineScope()

    val tabs = listOf(
        "Chats",
        "Channels"
    )

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = Color.Transparent,
        modifier = Modifier
            .zIndex(1f)
            .padding(horizontal = 10.dp)
            .height(Dimens.VPIndicatorHeight)
            .clip(CircleShape)
            .fillMaxWidth()
            .weight(1f)
            .border(
                width = 1.dp,
                shape = CircleShape,
                color = ifInDarkMode(
                    Colors.ViewPagerTextSelectedDarkTheme,
                    Colors.ViewPagerTextSelectedLightTheme
                )
            )
            .background(
                color = ifInDarkMode(
                    Colors.ViewPagerBackgroundDarkTheme,
                    Colors.ViewPagerBackgroundLightTheme
                ),
                shape = CircleShape
            ),
        indicator = { tabPositions ->
            Box(
                modifier = Modifier
                    .pagerTabIndicatorOffset(pagerState, tabPositions)
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(
                        color = ifInDarkMode(
                            Colors.ViewPagerIndicatorDarkTheme,
                            Colors.ViewPagerIndicatorLightTheme
                        ),
                        shape = CircleShape
                    )
            )
        }
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                modifier = Modifier
                    .zIndex(2F)
                    .background(Color.Transparent, shape = CircleShape),
                selected = pagerState.currentPage == index,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }, text = {
                    Text(
                        modifier = Modifier.background(Color.Transparent),
                        text = title,
                        fontSize = Dimens.FontSizeBig,
                        fontWeight = FontWeight.Bold,
                        color = ifInDarkMode(
                            if (index == pagerState.currentPage)
                                Colors.ViewPagerTextSelectedDarkTheme
                            else
                                Colors.ViewPagerTextUnselectedDarkTheme,

                            if (index == pagerState.currentPage)
                                Colors.ViewPagerTextSelectedLightTheme
                            else
                                Colors.ViewPagerTextUnselectedLightTheme
                        )
                    )
                })
        }
    }
}

@ExperimentalCoroutinesApi
@FlowPreview
@ExperimentalStdlibApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalPagerApi
@Composable
private fun Pager(
    navController: NavController,
    pagerState: PagerState,
    chatsViewModel: ChatsViewModel,
    channelsViewModel: ChannelsViewModel,
    isFabVisible: MutableState<Boolean>,
    isLoadingInProgress : MutableState<Boolean>
) {

    Box {

        HorizontalShadowTop()

        HorizontalPager(
            count = 2,
            state = pagerState,
            flingBehavior = PagerDefaults.flingBehavior(
                state = pagerState,
                snapAnimationSpec = spring(stiffness = 100f)
            )
        ) { page ->

            if (page == 0) {
                ChatsListWidget(
                    navController = navController,
                    selectionState = chatsViewModel.selectionState,
                    chatsViewModel = chatsViewModel,
                    isFabVisible = isFabVisible,
                    isLoadingInProgress = isLoadingInProgress
                )
            } else {
                ChannelsListWidget(
                    navController = navController,
                    selectionState = channelsViewModel.selectionState,
                    channelsViewModel = channelsViewModel,
                    isFabVisible = isFabVisible,
                    isLoadingInProgress = isLoadingInProgress
                )
            }
        }
        HorizontalShadowBottom()
    }
}

@ExperimentalCoroutinesApi
@FlowPreview
@ExperimentalStdlibApi
@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
private fun AllFabs(
    pagerState: PagerState,
    isFabVisible: MutableState<Boolean>
) {

    val fabBackgroundAnimator by remember {
        mutableStateOf(
            ValueAnimator.ofArgb(
                Colors.Chats.toArgb(),
                Colors.Channels.toArgb()
            )
        )
    }


    val fabBackground = animateColorAsState(
        targetValue = Color(
            fabBackgroundAnimator.apply {
                setCurrentFraction(pagerState.let {
                    it.currentPageOffset + it.currentPage
                })
            }.animatedValue as Int
        )
    )

    var childFabsVisible by remember {
        mutableStateOf(false)
    }

    val fabRotation by animateFloatAsState(
        90f * pagerState.currentPageOffset +
                90 * pagerState.currentPage +
                if (childFabsVisible) 45f else 0f,

    )


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        ChildFabs(
            isChildFabsVisible = childFabsVisible,
            setChildFabsVisible = { childFabsVisible = it },
        )

        var vibrate by remember {
            mutableStateOf(false)
        }

        if (vibrate) {
            LocalHapticFeedback.current
                .performHapticFeedback(HapticFeedbackType.LongPress)
            vibrate = false
        }

        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(Dimens.FabMargin)
                .zIndex(2f),
            visible = isFabVisible.value,
            ) {


            FloatingActionButton(
                modifier = Modifier
                    .rotate(fabRotation)
                    .size(Dimens.FabSizeDefault),

                onClick = {
                    vibrate = true
                    childFabsVisible = !childFabsVisible
                },
                backgroundColor =
                if (!childFabsVisible)
                    fabBackground.value
                else
                    Colors.White

            ) {
                Icon(
                    modifier = Modifier.size(40.dp),
                    imageVector = Icons.Rounded.Add,
                    tint = MaterialTheme.colors.surface,
                    contentDescription = "Create",
                )
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
private fun ChildFabs(
    isChildFabsVisible : Boolean,
    setChildFabsVisible : (Boolean) -> Unit,
){
    AnimatedVisibility(
        visible = isChildFabsVisible,
        enter = fadeIn(animationSpec =
            tween(durationMillis = Integers.AnimDurationMedium,)
        ),
        exit = fadeOut(animationSpec =
            tween(durationMillis = Integers.AnimDurationMedium)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(
                    color = MaterialTheme.colors.background.copy(alpha = 0.5f)
                )
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            setChildFabsVisible(!isChildFabsVisible)
                        }
                    )
                }
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(
                        bottom = Dimens.FabMargin * 2 +
                                Dimens.FabSizeDefault,
                        end = Dimens.FabMargin +
                                (Dimens.FabSizeDefault - Dimens.FabSizeSmall) / 2
                    )
            ) {
                ChildFabAndText(
                    text = R.string.write_message,
                    fabIcon = Icons.Rounded.Mail,
                    fabBackground = Colors.SoftRed
                ) {
                }
                Spacer(modifier = Modifier.height(10.dp))
                ChildFabAndText(
                    text = R.string.create_group,
                    fabIcon = Icons.Rounded.Group,
                    fabBackground = Colors.SoftOrange
                ) {
                }
                Spacer(modifier = Modifier.height(10.dp))
                ChildFabAndText(
                    text = R.string.create_channel,
                    fabIcon = Icons.Rounded.Campaign,
                    fabBackground = Colors.SoftGreen
                ) {
                }
            }
        }

    }
}
@Composable
private fun ChildFabAndText(
    @StringRes text : Int,
    fabIcon : ImageVector,
    fabBackground : Color,
    onClick : () -> Unit
) {

    val fabTint = MaterialTheme.colors.surface
    val textShape = RoundedCornerShape(35)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colors.primaryVariant.copy(alpha = .4f),

                    shape = textShape
                )
                .clip(textShape)
                .clickable(onClick = onClick),
        ) {
            Text(
                modifier = Modifier
                    .padding(5.dp),
                text = stringResource(id = text),
                fontSize = MaterialTheme.typography.body1.fontSize,
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        FloatingActionButton(
            modifier = Modifier
                .size(Dimens.FabSizeSmall),
            backgroundColor = fabBackground,
            onClick = onClick
        ) {
            Icon(
                modifier = Modifier.padding(5.dp),
                imageVector = fabIcon,
                contentDescription = stringResource(id = text),
                tint = fabTint
            )
        }
    }
}
