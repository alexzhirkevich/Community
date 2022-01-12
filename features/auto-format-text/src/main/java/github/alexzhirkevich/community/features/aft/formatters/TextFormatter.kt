package github.alexzhirkevich.community.features.aft.formatters

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString

internal interface TextFormatter {

    fun format (text : AnnotatedString) : AnnotatedString

    fun createProcessor(text: AnnotatedString) : (Int) -> Unit
}

internal fun AnnotatedString.copy(
    builder : AnnotatedString.Builder.() -> Unit
) : AnnotatedString  = buildAnnotatedString {
        append(this@copy)
        builder(this)
    }