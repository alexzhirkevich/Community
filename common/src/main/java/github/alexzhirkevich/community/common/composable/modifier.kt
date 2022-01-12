package github.alexzhirkevich.community.common.composable

import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.sqrt

enum class HorizontalSide {
    LEFT,
    RIGHT
}

fun Modifier.scrollOnResize(lazyListState: LazyListState) =
    composed {

    var prevHeight by remember {
        mutableStateOf(Int.MAX_VALUE)
    }

    val scope = rememberCoroutineScope()

    onSizeChanged {
        val prev = prevHeight
        if (prev != Int.MAX_VALUE && abs(prev-it.height)>30) {
            scope.launch {
                if (!lazyListState.isLastItemVisible())
                    lazyListState.scrollBy((prev - it.height).toFloat())
            }
        }
        prevHeight = it.height
    }
}

fun Modifier.horizontalDrag(
    side : HorizontalSide = HorizontalSide.LEFT,
    enabled : Boolean = true,
    maxOffset : Dp = 100.dp,
    effectiveOffset : Dp = maxOffset/2,
    hapticFeedback: HapticFeedbackType? = null,
    animationSpec: AnimationSpec<Dp> = spring(
        dampingRatio = 0.5f,
        visibilityThreshold = Dp.VisibilityThreshold
    ),
    onDrag : (fraction : Float) -> Unit = {},
    onSuccess : () -> Unit = {}
) : Modifier = composed {

    val offset = remember {
        mutableStateOf(0.dp)
    }

    val animatedOffset by animateDpAsState(
        targetValue = offset.value,
        animationSpec = animationSpec
    )

    val needHapticFeedback = remember {
        mutableStateOf(true)
    }
    if (animatedOffset.value.absoluteValue > 0f) {
        onDrag(
            (animatedOffset / maxOffset)
                .absoluteValue
        )
    }
    val haptic = LocalHapticFeedback.current

    if (enabled) {
        pointerInput(Unit) {
            detectHorizontalDragGestures(
                onHorizontalDrag = { change, dragAmount ->
                    change.consumeAllChanges()
                    val newX = (offset.value + dragAmount.toDp())
                    if ((newX <= 0.dp && side == HorizontalSide.LEFT ||
                                newX >= 0.dp && side == HorizontalSide.RIGHT) &&
                        newX.value.absoluteValue <= maxOffset.value.absoluteValue
                    ) {
                        offset.value = newX
                        if (hapticFeedback != null) {
                            if (needHapticFeedback.value &&
                                newX.value.absoluteValue >= effectiveOffset.value.absoluteValue
                            ) {
                                needHapticFeedback.value = false
                                haptic.performHapticFeedback(hapticFeedback)
                            } else
                                if (!needHapticFeedback.value &&
                                    newX.value.absoluteValue <= effectiveOffset.value.absoluteValue
                                ) {
                                    needHapticFeedback.value = true
                                }
                        }
                    }
                },
                onDragEnd = {
                    if (offset.value.value.absoluteValue >= effectiveOffset.value.absoluteValue) {
                        onSuccess()
                    }
                    offset.value = 0.dp
                    needHapticFeedback.value = true
                })
        }.offset(x = animatedOffset, y = 0.dp)
    } else this
}


fun Modifier.spreadingBackground(
    color: Color,
    fromOffset: Offset = Offset.Unspecified,
    visible : Boolean = true,
    animationEnabled : Boolean = true,
    alpha : Float  = .3f,
    speed : Float= 300f,
) = composed {

    val diameter = remember {
        mutableStateOf(0f)
    }

    val diameterAnimation =  animateFloatAsState(
        targetValue = diameter.value,
        animationSpec = spring(stiffness = speed)
    )
    val diameterAnimated = if (animationEnabled)
       diameterAnimation.value
    else
        diameter.value

    val animationFinished = diameter.value == diameterAnimated

    drawBehind {
        clipRect {
            val offset = if (fromOffset == Offset.Unspecified)
                center
            else fromOffset

            diameter.value = if (visible) sqrt(
                size.width * size.width +
                        size.height * size.height
            ) * 2 else 0f

            drawCircle(
                color = color,
                alpha = alpha,
                center = offset,
                radius = diameterAnimated / 2,
            )
        }
    }
}