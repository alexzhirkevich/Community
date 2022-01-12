package github.alexzhirkevich.community.features.aft

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import github.alexzhirkevich.community.features.aft.formatters.FormatterList
import github.alexzhirkevich.community.features.aft.formatters.LinkTextFormatter
import github.alexzhirkevich.community.features.aft.formatters.MarkupFormatter
import github.alexzhirkevich.community.features.aft.formatters.TagTextFormatter
import github.alexzhirkevich.community.features.aft.formatters.TextFormatter

@Composable
fun AutoFormatText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    linkColor : Color = MaterialTheme.colors.secondary,
    style: TextStyle = LocalTextStyle.current,
    tagNavigator: TagNavigator = EmptyTagNavigator,
) {
    val style = style.merge(
        TextStyle(
            color = color,
            fontSize = fontSize,
            fontWeight = fontWeight,
            fontStyle = fontStyle,
            fontFamily = fontFamily,
            letterSpacing = letterSpacing,
            textDecoration = textDecoration,
            textAlign = textAlign,
            lineHeight = lineHeight
        )
    )

    val formatter: TextFormatter = rememberTextFormatter(
        linkColor = linkColor,
        style = style,
        tagNavigator = tagNavigator
    )


    val string = formatter.format(buildAnnotatedString {
        append(text)
    })

    ClickableText(
        modifier = modifier,
        text = string,
        onClick = formatter.createProcessor(string),
        style = style,
        softWrap = softWrap,
        overflow = overflow,
        maxLines = maxLines,
        onTextLayout = onTextLayout,
    )
}

@Composable
internal fun rememberTextFormatter(
    linkColor: Color = Color.Blue,
    style: TextStyle = LocalTextStyle.current,
    uriHandler: UriHandler = LocalUriHandler.current,
    tagNavigator: TagNavigator = EmptyTagNavigator
) : TextFormatter {

    val scope = rememberCoroutineScope()

    return remember {

        val linkStyle = style.toSpanStyle().copy(
            color = linkColor,
            textDecoration = if (style.textDecoration != null)
                TextDecoration.combine(
                    listOf(
                        style.textDecoration!!,
                        TextDecoration.Underline
                    )
                ) else TextDecoration.Underline
        )

        val tagStyle = style.toSpanStyle().copy(color = linkColor)



        FormatterList(
            MarkupFormatter(style.toSpanStyle()),
            LinkTextFormatter(linkStyle, uriHandler),
            TagTextFormatter(tagStyle, tagNavigator, scope),
        )
    }
}
