package github.alexzhirkevich.community.common.composable

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.launch


fun String.toPlaceholderText() =
    split(" ").mapNotNull {
        if (it.isNotEmpty())
            it[0]
        else null
    }.take(2).joinToString(separator = "")

fun LazyListState.isLastItemVisible() : Boolean =
    layoutInfo.totalItemsCount == 0 || layoutInfo.visibleItemsInfo.lastOrNull()?.index ==
            layoutInfo.totalItemsCount -1


fun LazyListState.setScrollingEnabled(isEnabled : Boolean, scope: CoroutineScope) {
    if (isEnabled)
        Log.i("SCROLL", "TRY ENABLED")

    scope.launch {
        try {
            scroll(scrollPriority = MutatePriority.PreventUserInput) {
                if (!isEnabled) {
                    Log.i("SCROLL", "DISABLED")
                    awaitCancellation()
                }
            }
        } catch (t: CancellationException) {
            if (!isEnabled) Log.i("SCROLL", "ENABLED")
        }
    }
}