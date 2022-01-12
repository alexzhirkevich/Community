package github.alexzhirkevich.community.features.stickyheader

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

class HeaderProperties(
    private val lazyListState: LazyListState,
    private val headerIndeces : Map<String,Int>
    ) {

    fun isSticky(currentHeaderKey : String) : Boolean{
        return headerIndeces[currentHeaderKey]?.let {
            it < lazyListState.firstVisibleItemIndex
        } ?: false
    }
}

@Composable
fun <T> rememberHeaderProperties(
    lazyListState: LazyListState,
    allItems : List<T>,
    text : (T) -> String
) : HeaderProperties = remember {
    val m = allItems.groupBy {
        text(it)
    }.map {
        val firstIdx = it.value.firstOrNull()?.let {
            allItems.indexOf(it)
        } ?: 0
        it.key to firstIdx
    }.toMap()

    val headerIndeces = m.map {
        it.key to it.value + m.keys.indexOf(it.key)
    }.toMap()

    HeaderProperties(lazyListState, headerIndeces)
}
