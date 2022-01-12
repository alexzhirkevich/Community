package github.alexzhirkevich.community.features.aft.formatters

import androidx.compose.ui.text.AnnotatedString

internal class FormatterList(private vararg val formatters : TextFormatter) :
    List<TextFormatter> by formatters.toList(), TextFormatter {

    override fun format(text: AnnotatedString): AnnotatedString {
        var mText = text
        formatters.forEach {
            mText = it.format(mText)
        }

        return mText
    }

    override fun createProcessor(text: AnnotatedString): (Int) -> Unit = { idx ->
        formatters.forEach {
            it.createProcessor(text).invoke(idx)
        }
    }
}