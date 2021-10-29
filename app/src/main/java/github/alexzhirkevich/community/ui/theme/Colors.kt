package github.alexzhirkevich.community.ui.theme


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color



object Colors {
    val White: Color = Color.White
    val Black: Color = Color.Black
    val Shadow: Color = Color(0xFF151515)

    val Purple200 = Color(0xFFBB86FC)
    val Purple500 = Color(0xFF6200EE)
    val Purple700 = Color(0xFF3700B3)
    val Teal200 = Color(0xFF03DAC5)
    val Gray = Color(0xFF919191)
    val DarkGray = Color(0xFF353535)
    val Red = Color(0xFFe04006)
    val Blue = Color(0xFF548ec4)
    val Chats = Blue
    val Channels = Red

    val SoftRed = Color(0xffff5961)
    val SoftOrange = Color(0xffFFA500)
    val SoftGreen = Color(0xff8fff93)

    val BackgroundDarkTheme = Color(0xFF272727)
    val BackgroundLightTheme = Color.White

    val ViewPagerBackgroundDarkTheme = Black
    val ViewPagerIndicatorDarkTheme = White
    val ViewPagerTextSelectedDarkTheme = Black
    val ViewPagerTextUnselectedDarkTheme = White

    val ViewPagerBackgroundLightTheme = Gray
    val ViewPagerIndicatorLightTheme = White
    val ViewPagerTextSelectedLightTheme = Black
    val ViewPagerTextUnselectedLightTheme = White

    val TextDarkTheme = Color.White
    val TextLightTheme = Color.Black
    val TextSecondary = Gray
}