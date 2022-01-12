package github.alexzhirkevich.community.features.aft.formatters

import android.util.Log
import android.util.Patterns
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import github.alexzhirkevich.community.features.aft.BuildConfig

internal class LinkTextFormatter (
    private val spanStyle : SpanStyle,
    private val uriHandler: UriHandler
) : TextFormatter {

    override fun format(text: AnnotatedString): AnnotatedString = text.copy {

        val urls = text.text.split(" ")
            .filter {
                Patterns.WEB_URL.pattern()
                Patterns.WEB_URL.matcher(it).find()
            }

        urls.forEach {
            val start = text.indexOf(it)
           // if (text.getStringAnnotations(start = start, start + it.length).isEmpty()) {
                addStyle(
                    spanStyle, start = start, end = start + it.length
                )
                addStringAnnotation(
                    tag = tag,
                    annotation = it,
                    start = start,
                    end = start + it.length
                )
          //  }
        }
    }

    override fun createProcessor(text: AnnotatedString): (Int) -> Unit = {
        text.getStringAnnotations(tag, it, it)
            .firstOrNull()?.let {
                kotlin.runCatching {
                    uriHandler.openUri(
                        if ("://" in it.item)
                            it.item
                        else "https://${it.item}"
                    )
                }.onFailure {
                    if (BuildConfig.DEBUG) {
                        Log.e(javaClass.simpleName,
                            "Failed to open link", it)
                    }
                }
            }
    }
    companion object {
        val tag: String
            get() = this::class.java.simpleName
    }
}