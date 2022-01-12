package github.alexzhirkevich.community.routing

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

internal const val ChangeRouteDuration = 500

internal val SlideInFromLeft = slideInHorizontally(
    initialOffsetX = { -it },
    animationSpec = tween(ChangeRouteDuration)
)

internal val SlideInFromRight = slideInHorizontally(
    initialOffsetX = { it },
    animationSpec = tween(ChangeRouteDuration)
)

internal val SlideOutToLeft = slideOutHorizontally(
    targetOffsetX = { -it },
    animationSpec = tween(ChangeRouteDuration)
)

internal val SlideOutToRight = slideOutHorizontally(
    targetOffsetX = { it },
    animationSpec = tween(ChangeRouteDuration)
)