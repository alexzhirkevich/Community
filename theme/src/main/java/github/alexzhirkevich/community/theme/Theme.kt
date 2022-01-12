package github.alexzhirkevich.community.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.flow.MutableStateFlow

internal val GothamProFamily = FontFamily(
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
    primaryVariant = Colors.Shadow,
    secondary = Colors.Blue,
    secondaryVariant = Colors.Gray,
    background = Colors.BackgroundDarkTheme,
    surface = Colors.White,
    error = Colors.Error
)

private val LightColorPalette = lightColors(
    primary = Colors.BackgroundLightTheme,
    primaryVariant = Colors.DarkGray,
    secondary = Colors.Gray,
    secondaryVariant = Colors.Gray,
    background = Colors.BackgroundLightTheme,
    surface = Colors.Blue,
    error = Colors.Error

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
    if (MainTheme.isDark)
        t
    else
        f


object MainTheme {

    private val forcedTheme = mutableStateOf<ForcedTheme>(ForcedTheme.None)

    fun forceTheme(theme: ForcedTheme) {
        forcedTheme.value = theme
    }


    val isDark : Boolean
    @Composable
    get () = forcedTheme.value == ForcedTheme.Dark ||
            (forcedTheme.value == ForcedTheme.None && isSystemInDarkTheme())
    @Composable
    fun setContent(content: @Composable() () -> Unit) {
        val systemUiController = rememberSystemUiController()



        val colors = if (isDark) {
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
            typography = Typography(),
            shapes = Shapes,
            content = {

                val selectionColor = MaterialTheme.colors.secondaryVariant.copy(
                    alpha = .9f
                )
                val textSelectionColors = TextSelectionColors(
                    handleColor = selectionColor,
                    backgroundColor = selectionColor,
                )

                CompositionLocalProvider(
                    LocalTextSelectionColors provides textSelectionColors,
                ) {
                    ProvideWindowInsets {
                        ProvideTextStyle(
                            value = TextStyle(
                                color = if (isDark)
                                    Colors.TextDarkTheme
                                else
                                    Colors.TextLightTheme,
                                fontFamily = GothamProFamily,
                                fontSize = Dimens.FontSizeMedium,
                                lineHeight =  Dimens.FontSizeMedium *1.3
                            ),
                            content = content,

                        )
                    }
                }
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
        typography = Typography(),
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