package github.alexzhirkevich.community.theme

import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
@Composable
fun Typography(): Typography {
    return Typography(
        body1 = TextStyle(
            fontFamily = GothamProFamily,
            lineHeight = Dimens.FontSizeMedium * 1.3,
            fontSize = Dimens.FontSizeMedium,
            color = ifInDarkMode(
                Colors.TextDarkTheme, Colors.TextLightTheme)
        ),
        body2 = TextStyle(
            fontFamily = GothamProFamily,
            fontSize = Dimens.FontSizeSmall,
            lineHeight = Dimens.FontSizeSmall * 1.3,
            color = Colors.Gray
        ),
        /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
    )
}