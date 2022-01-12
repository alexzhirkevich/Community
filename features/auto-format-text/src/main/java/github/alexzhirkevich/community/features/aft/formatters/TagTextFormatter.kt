package github.alexzhirkevich.community.features.aft.formatters

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import github.alexzhirkevich.community.features.aft.TagNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal class TagTextFormatter(
    private val spanStyle: SpanStyle,
    private val tagNavigator: TagNavigator,
    private val scope : CoroutineScope
    ) : TextFormatter {
    override fun format(text: AnnotatedString): AnnotatedString = text.copy {

        val delimiters = text.filterNot {
            it.isLetterOrDigit() || it=='@'
        }.toString().toCharArray()

        val splits = (if (delimiters.isNotEmpty())
            text.split(*delimiters)
        else listOf(text.text))
            .filter { it.startsWith('@') }

        splits.forEach {
            val start = text.indexOf(it)
          //  if (text.getStringAnnotations(start = start, start + it.length).isEmpty()) {
                addStyle(
                    spanStyle, start = start, end = start + it.length
                )
                addStringAnnotation(
                    tag = tag,
                    annotation = it,
                    start = start,
                    end = start + it.length
                )
            }
       // }
    }

    override fun createProcessor(text: AnnotatedString): (Int) -> Unit = {
        text.getStringAnnotations(tag, it, it)
            .firstOrNull()?.let {
                scope.launch {
                    tagNavigator.navigate(it.item.removePrefix("@"))
                }
            }
    }

    companion object  {
        val tag: String
            get() = TagTextFormatter::class.java.simpleName
    }
}