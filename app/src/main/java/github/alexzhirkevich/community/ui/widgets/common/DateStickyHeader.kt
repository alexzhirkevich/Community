package github.alexzhirkevich.community.ui.widgets.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import github.alexzhirkevich.community.ui.theme.Colors
import github.alexzhirkevich.community.ui.theme.Dimens
import github.alexzhirkevich.community.ui.theme.Integers

@ExperimentalAnimationApi
@Composable
fun DateStickyHeader(
    time : String,
    lazyListState: LazyListState,
    fadeout : Boolean
) {

    val targetAlpha = if (lazyListState.isScrollInProgress) 0.7f else 0f


    AnimatedVisibility(
        modifier = Modifier
            .fillMaxWidth(),
        visible = if (fadeout) lazyListState.isScrollInProgress else true,
        enter = fadeIn(
            animationSpec = tween(
                durationMillis = Integers.AnimDurationSmall
            )
        ),
        exit = fadeOut(
            animationSpec = tween(
                durationMillis = Integers.AnimDurationLong,
                delayMillis = Integers.AnimFadeDelay
            )
        )
    ) {

        Box(
            modifier = Modifier
                .padding(top = 5.dp)
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .background(Colors.Shadow, CircleShape)
                    .padding(
                        horizontal = 10.dp,
                        vertical = 5.dp
                    ),
                fontSize = Dimens.FontSizeMedium,
                text = time
            )

        }
    }
}