package github.alexzhirkevich.community.common.composable

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.*

@ExperimentalAnimationApi
class SharedContent(
    val contentTransform: ContentTransform?=null,
    val content : @Composable () -> Unit) {
}
