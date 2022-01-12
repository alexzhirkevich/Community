package github.alexzhirkevich.community.common.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.*
import com.skydoves.landscapist.Shimmer
import github.alexzhirkevich.community.features.aft.AutoFormatText
import github.alexzhirkevich.community.features.aft.TagNavigator
import github.alexzhirkevich.community.theme.Dimens
import github.alexzhirkevich.community.theme.Integers

@Composable
fun DetailMenuItem(
    icon : ImageVector,
    text : String,
    label : String,
    format : Boolean = false,
    tagNavigator: TagNavigator?= null,
    onClick : () -> Unit = {}
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 15.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colors.secondaryVariant,
            modifier = Modifier.padding(
                end = 15.dp,
                top = 10.dp,
                bottom = 10.dp
            )
        )
        Column(
            Modifier.padding(vertical = 5.dp)
        ) {
            if (format) {
                AutoFormatText(
                    text = text,
                    style = MaterialTheme.typography.body1,
                    linkColor = MaterialTheme.colors.secondary,
                    tagNavigator = tagNavigator!!
                )
            } else {
                Text(
                    text = text,
                    style = MaterialTheme.typography.body1,
                )
            }
            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = label,
                style = MaterialTheme.typography.body2
            )
        }
    }
}

@Composable
fun DetailMenuItemShimmer() = with(LocalDensity.current){

    val shape = remember {
        DetailMenuItemShimmerShape()
    }
    Box(Modifier
        .padding(horizontal = 15.dp, vertical = 10.dp),
    ) {
        Shimmer(
            baseColor = MaterialTheme.colors.background,
            highlightColor = MaterialTheme.colors.secondaryVariant,
            durationMillis = Integers.AnimDurationLong*2,
            modifier = Modifier
                .height(DetailMenuItemShimmerShape.height())
                .width(200.dp)
                .clip(shape)
        )
    }
}


private class DetailMenuItemShimmerShape : Shape {

    companion object {
        @Composable
        fun height(): Dp = with(LocalDensity.current) {
            return Dimens.FontSizeMedium.toDp() +
                    Dimens.FontSizeSmall.toDp() +
                    5.dp
        }
    }

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            path = Path().apply {
                reset()
                val iconStart = 0f
                val iconSize = 24 * density.density
                val iconTop = (size.height-iconSize)/2

                addOval(
                    Rect(
                        left = iconStart,
                        top = iconTop,
                        right = iconSize,
                        bottom = iconTop + iconSize
                    )
                )

                val textLeft = iconSize + 15 * density.density
                val textSize = Dimens.FontSizeMedium.value * density.density * density.fontScale
                addRoundRect(
                    RoundRect(
                        left = textLeft,
                        top = 0f,
                        right = size.width,
                        bottom = textSize,
                        cornerRadius = CornerRadius(textSize/2,textSize/2)
                    )
                )
                val textSizeSmallTop = textSize + 5 * density.density
                val textSizeSmall = Dimens.FontSizeSmall.value * density.density * density.fontScale

                addRoundRect(
                    RoundRect(
                        left = textLeft,
                        top = textSizeSmallTop,
                        right = textLeft + size.width/3 ,
                        bottom = textSizeSmallTop + textSizeSmall,
                        cornerRadius = CornerRadius(textSizeSmall / 2, textSizeSmall / 2)
                    )
                )
            }
        )
    }
}
