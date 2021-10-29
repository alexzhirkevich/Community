package github.alexzhirkevich.community.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import github.alexzhirkevich.community.R

private val GothamProFamily = FontFamily(
    Font(R.font.gotham_pro),
    Font(R.font.gotham_pro_bold, weight = FontWeight.Bold),
    Font(R.font.gotham_pro_light, weight = FontWeight.Light),
    Font(R.font.gotham_pro_black, weight = FontWeight.Black),
    Font(R.font.gotham_pro_medium, weight = FontWeight.Medium),
    Font(R.font.gotham_pro_bold_italic, weight = FontWeight.Bold, style = FontStyle.Italic),
    Font(R.font.gotham_pro_light_italic, weight = FontWeight.Light, style = FontStyle.Italic),
    Font(R.font.gotham_pro_black_italic, weight = FontWeight.Black, style = FontStyle.Italic),
    Font(R.font.gotham_pro_medium_italic, weight = FontWeight.Medium, style = FontStyle.Italic),
)

private val DarkColorPalette = darkColors(
    primary = Colors.BackgroundDarkTheme,
    primaryVariant = Colors.BackgroundDarkTheme,
    secondary = Colors.White,
    background = Colors.BackgroundDarkTheme,
)

private val LightColorPalette = lightColors(
    primary = Colors.BackgroundDarkTheme,
    primaryVariant = Colors.BackgroundLightTheme,
    secondary = Colors.Black,
    background = Colors.BackgroundLightTheme

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

sealed interface ForcedTheme{
    object Dark : ForcedTheme
    object Light : ForcedTheme
    object None : ForcedTheme
}

@Composable
fun <T> ifInDarkMode(t : T, f :T) : T=
    if (MainTheme.isDark())
        t
    else
        f


object MainTheme {

    private val forcedTheme = mutableStateOf<ForcedTheme>(ForcedTheme.None)

    @Composable
    fun ForceTheme(theme: ForcedTheme) {
        forcedTheme.value = theme
    }

    @Composable
    fun isDark(): Boolean = true
//        forcedTheme.value is ForcedTheme.Dark ||
//                (forcedTheme.value is ForcedTheme.None && isSystemInDarkTheme())

    @Composable
    fun SetContent(content: @Composable() () -> Unit) {
        val systemUiController = rememberSystemUiController()



        val colors = if (isDark()) {
            with(systemUiController) {
                setNavigationBarColor(
                    DarkColorPalette.background,
                    darkIcons = false
                )
                setStatusBarColor(
                    DarkColorPalette.background,
                    darkIcons = false
                )
            }
            DarkColorPalette

        } else {
            with(systemUiController) {
                setNavigationBarColor(
                    LightColorPalette.background,
                    darkIcons = true
                )
                setStatusBarColor(
                    LightColorPalette.background,
                    darkIcons = true
                )
            }
            LightColorPalette
        }

//    systemUiController.setSystemBarsColor(
//        color = colors.background
//    )

        MaterialTheme(
            colors = colors,
            typography = Typography,
            shapes = Shapes,
            content = {
                ProvideTextStyle(
                    value = TextStyle(
                        color = if (isDark())
                            Colors.TextDarkTheme
                        else
                            Colors.TextLightTheme,
                        fontFamily = GothamProFamily
                    ),
                    content = content
                )

            },
        )
    }
}

@Composable
fun MainTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {

    val systemUiController = rememberSystemUiController()


    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

//    systemUiController.setSystemBarsColor(
//        color = colors.background
//    )

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = {
            ProvideTextStyle(
                value = TextStyle(
                    color = if (darkTheme)
                        Colors.TextDarkTheme
                    else
                        Colors.TextLightTheme,
                    fontFamily = GothamProFamily
                ),
                content = content
            )

        },
    )
}