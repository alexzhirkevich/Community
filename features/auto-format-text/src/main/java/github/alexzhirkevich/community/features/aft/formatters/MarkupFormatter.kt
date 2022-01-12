package github.alexzhirkevich.community.features.aft.formatters

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration

internal class MarkupFormatter(textStyle: SpanStyle) : TextFormatter {

    private val underlineSpanStyle = textStyle.copy(
        textDecoration = TextDecoration.Underline
    )

    private val crosslineSpanStyle = textStyle.copy(
        textDecoration = TextDecoration.LineThrough
    )

    private val italicSpanStyle = textStyle.copy(
        fontStyle =  FontStyle.Italic
    )

    private val boldSpanStyle = textStyle.copy(
        fontWeight =  FontWeight.Bold
    )

    override fun format(text: AnnotatedString): AnnotatedString =
        text
            .formatInside("__", underlineSpanStyle)
            .formatInside("//", italicSpanStyle)
            .formatInside("**", boldSpanStyle)
            .formatInside("--", crosslineSpanStyle)


    private fun AnnotatedString.formatInside(seq: String, textStyle: SpanStyle): AnnotatedString {

        val transforms = mutableListOf<AnnotatedString.Range<SpanStyle>>()

        var text = this as CharSequence

        var start = text.indexOf(seq)

        kotlin.runCatching {
            while (start != -1) {
                val end = text.indexOf(seq, start + 1)
                start = if (end != -1) {
                    if (end != start + 1) {
                        text = text.removeRange(start, start + seq.length)
                        text = text.removeRange(end - seq.length, end)
                        transforms.add(
                            AnnotatedString.Range(
                                textStyle, start, end - seq.length
                            )
                        )
                        text.indexOf(seq, end - seq.length)
                    } else end + 1
                } else -1
            }
        }.onFailure { transforms.clear() }

        val mSpanStyles = spanStyles.map { range ->

            var start = range.start
            var end = range.end

            transforms.forEach {

                if (it.start < start + seq.length) {
                    start -= seq.length
                    end -= seq.length
                }
                if (it.end < start + seq.length) {
                    start -= seq.length
                    end -= seq.length
                }
                if (it.end in (start + 1) until end) {
                    end -= seq.length
                }
            }
            return@map range.copy(start = start, end = end)
        }.toMutableList()

        mSpanStyles.addAll(transforms)

        return AnnotatedString(text.toString(), mSpanStyles)
    }


    override fun createProcessor(text: AnnotatedString): (Int) -> Unit = {

    }

    companion object  {
        val tag: String
            get() = MarkupFormatter::class.java.simpleName
    }

}