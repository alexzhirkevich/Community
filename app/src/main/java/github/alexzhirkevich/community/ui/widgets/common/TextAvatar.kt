package github.alexzhirkevich.community.ui.widgets.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import github.alexzhirkevich.community.ui.theme.Colors
import github.alexzhirkevich.community.ui.theme.Dimens

@Composable
fun AvatarTextPlaceholder(text: String, color: Color, fontSize: TextUnit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(CircleShape)
            .border(Dimens.AvatarBorderSize, Color.White, CircleShape)
            .background(color = Colors.White),
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.Center),
            text = text,
            color = color,
            fontSize = fontSize,
            style = TextStyle(fontWeight = FontWeight.Bold)
        )
    }
}