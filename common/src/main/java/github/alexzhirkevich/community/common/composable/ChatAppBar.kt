package github.alexzhirkevich.community.common.composable

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.skydoves.landscapist.glide.GlideImage
import github.alexzhirkevich.community.theme.Colors
import github.alexzhirkevich.community.theme.Dimens
import github.alexzhirkevich.community.theme.Integers
import me.onebone.toolbar.CollapsingToolbarScope
import me.onebone.toolbar.CollapsingToolbarState
import kotlin.math.roundToInt


@Composable
fun ChatAppBar(
    modifier: Modifier = Modifier,
    imageUri: String = "",
    name: String = "",
    description: String = "",
    color: Color = Color.Black,
    onClick: (() -> Unit) ?=null
) {


    val requestOptions = with(LocalDensity.current) {
        remember {
            RequestOptions()
                .centerCrop()
                .override(Dimens.AvatarSizeSmall.roundToPx())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .encodeQuality(25)
        }
    }

    TopAppBar(
        backgroundColor = MaterialTheme.colors.background,
        modifier = modifier
    ) {
        NavigateBackIconButton()

        val placeHolder = @Composable {
            AvatarTextPlaceholder(
                text = name.toPlaceholderText(),
                color = color,
                fontSize = MaterialTheme.typography.body1.fontSize,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(color = Colors.White)
            )
        }

        Row(
            modifier = Modifier
            .clip(CircleShape)
                .let {
                    if (onClick != null) {
                        it.clickable(onClick = onClick)
                    } else it
                },
            verticalAlignment = Alignment.CenterVertically
        ) {

            GlideImage(
                imageModel = imageUri,
                modifier = Modifier
                    .size(Dimens.AvatarSizeSmall)
                    .clip(CircleShape)
                    .border(Dimens.AvatarBorderSize, MaterialTheme.colors.surface, CircleShape),
                requestOptions = { requestOptions },
                failure = { placeHolder() },
                loading = { placeHolder() }
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.secondaryVariant
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
        }
    }
}

const val CollapseAvatarProgress = .3

@Composable
fun CollapsingToolbarScope.CollapsingChatAppBar(
    modifier: Modifier = Modifier,
    imageUri: String = "",
    name: String = "",
    description: String = "",
    color: Color = Color.Black,
    toolbarState: CollapsingToolbarState,
    scrollState: ScrollableState,
    onImageClick: () -> Unit = {}
) {

    var initial by rememberSaveable {
        mutableStateOf(true)
    }
    LaunchedEffect(key1 = scrollState.isScrollInProgress){
        if (!initial && !scrollState.isScrollInProgress &&
            toolbarState.progress>0f &&
            toolbarState.progress<= CollapseAvatarProgress)
            toolbarState.collapse(Integers.AnimDurationSmall)
        initial = false
    }

    val imageHeight = with(LocalDensity.current) {
        (LocalContext.current.resources
            .displayMetrics.widthPixels * 9f / 16f)
            .toDp()
    }
    val imageWidth = LocalContext.current.resources.displayMetrics.widthPixels

    val smallAvatarRequestOptions = with (LocalDensity.current) {
        remember {
            RequestOptions()
                .centerCrop()
                .override(Dimens.AvatarSizeSmall.roundToPx())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .encodeQuality(25)
        }
    }


    val largeAvatarRequestOptions = with (LocalDensity.current) {
        remember {
            RequestOptions()
                .centerCrop()
                .override(imageWidth,imageHeight.roundToPx())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
        }
    }

//    LaunchedEffect(key1 = state.isScrollInProgress){
//        Log.e("Scroll",state.isScrollInProgress.toString())
//        if (!state.isScrollInProgress && state.progress <= CollapseAvatarProgress)
//            state.collapse(Integers.AnimDurationSmall)
//    }

    val isBigImageVisible = toolbarState.progress > CollapseAvatarProgress
    TopAppBar(
        backgroundColor = Color.Transparent,
        elevation = 0.dp,
    ) {}
    GlideImage(
        modifier = Modifier
            .fillMaxWidth()
            .height(imageHeight)
            .parallax()
            .alpha(if (isBigImageVisible) toolbarState.progress else 0f)
            .let {
                if (isBigImageVisible) {
                    it.clickable(onClick = onImageClick)
                } else it
            },
        imageModel = imageUri,
        requestOptions = { largeAvatarRequestOptions },
        loading = { PlaceHolder(name, color) },
        failure = { PlaceHolder(name, color) }
    )


    Box(modifier) {

        var textInfoHeight by remember {
            mutableStateOf(0)
        }


        NavigateBackIconButton(
            modifier = Modifier
                .zIndex(1f)
                .offset(x = 4.dp, y = 16.dp)
        )


        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight)
        )

        var offsetX by with(LocalDensity.current) {
            rememberSaveable {
                mutableStateOf(if (toolbarState.progress == 0f)
                    (Dimens.ToolbarBtnSize+30.dp).roundToPx().toFloat() else 0f)
            }
        }
        val offsetAnimator = animateFloatAsState(targetValue = offsetX)

        with(LocalDensity.current) {
            LaunchedEffect(key1 = isBigImageVisible) {
                offsetX = (if (!isBigImageVisible)
                    (Dimens.ToolbarBtnSize+30.dp).roundToPx().toFloat()
                else 0f) + 5.dp.roundToPx()
            }
        }

        Row(
            modifier = Modifier
                .zIndex(1f)
                .padding(horizontal = 5.dp)
                .onGloballyPositioned {
                    textInfoHeight = it.size.height
                }
                .offset {
                    val y = (toolbarState.height - textInfoHeight - 7.dp.roundToPx())

                    IntOffset(offsetAnimator.value.roundToInt(), y)
                }
                .clip(CircleShape),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!isBigImageVisible) {
                GlideImage(
                    modifier = Modifier
                        .size(Dimens.AvatarSizeSmall)
                        .clip(CircleShape)
                        .clickable(onClick = onImageClick)
                        .border(Dimens.AvatarBorderSize, MaterialTheme.colors.surface, CircleShape),
                    imageModel = imageUri,
                    requestOptions = { smallAvatarRequestOptions },
                    loading = { PlaceHolder(name, color) },
                    failure = { PlaceHolder(name, color) }
                )
            }
            Column(
                modifier = Modifier.padding(start = 10.dp),
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier
                    //   .background(MaterialTheme.colors.background.copy(alpha = .8f))
                    //   .clip(CircleShape)
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier
                    //.background(MaterialTheme.colors.background.copy(alpha = .8f))
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
        }
    }
}
@Composable
fun PlaceHolder(name : String, color: Color) {
    AvatarTextPlaceholder(
        text = name.toPlaceholderText(),
        color = color,
        fontSize = MaterialTheme.typography.body1.fontSize,
        modifier = Modifier
            .fillMaxSize()
            .background(color = Colors.White)
    )
}