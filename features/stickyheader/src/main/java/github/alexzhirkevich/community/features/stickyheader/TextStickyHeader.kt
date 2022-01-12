package github.alexzhirkevich.community.features.stickyheader

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp


@ExperimentalAnimationApi
@Composable
fun TextStickyHeader(
    lazyListState: LazyListState,
    text : String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = TextUnit.Unspecified,
    textColor: Color = Color.Unspecified,
    backgroundColor : Color = Color.Unspecified,
    fadeout : Boolean = false,
    enter : EnterTransition = fadeIn(
        animationSpec = tween(
            durationMillis = 500
        )
    ),
    exit : ExitTransition = fadeOut(
        animationSpec = tween(
            durationMillis = 500,
            delayMillis = 2000
        )
    ),
) {

    AnimatedVisibility(
        modifier = modifier
            .fillMaxWidth(),
        visible = if (fadeout)
            lazyListState.isScrollInProgress
            else true,
        enter = enter,
        exit = exit
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .background(backgroundColor, CircleShape)
                    .padding(
                        horizontal = 10.dp,
                        vertical = 5.dp
                    ),
                fontSize = fontSize,
                text = text,
                color = textColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


