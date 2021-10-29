package github.alexzhirkevich.community.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import github.alexzhirkevich.community.ui.theme.Colors

@Composable
fun BoxScope.HorizontalShadowTop(){
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(10.dp)
            .align(Alignment.TopCenter)
            .zIndex(Float.MAX_VALUE)
            .alpha(.5f)
            .background(
                brush = Brush.verticalGradient(
                    0f to Colors.Shadow,
                    1f to Color.Transparent
                )
            )
    )
}

@Composable
fun BoxScope.HorizontalShadowBottom(height : Dp = 10.dp){
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .align(Alignment.BottomCenter)
            .zIndex(Float.MAX_VALUE)
            .alpha(.5f)
            .background(
                brush = Brush.verticalGradient(
                    0f to Color.Transparent,
                    1f to Colors.Shadow,
                )
            )
    )
}