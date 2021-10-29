package github.alexzhirkevich.community

import androidx.compose.runtime.Composable
import github.alexzhirkevich.community.core.entities.interfaces.SystemMessage

fun String.toPlaceholderText() =
    split(" ").mapNotNull {
        if (it.isNotEmpty())
            it[0]
        else null
    }.take(2).joinToString(separator = "")
