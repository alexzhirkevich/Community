package github.alexzhirkevich.community.common.composable

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.core.os.bundleOf

@Composable
fun rememberSelectionState() : SelectionState =
    rememberSaveable(saver = SelectionStateSaver()) {
    SelectionState()
}

private class SelectionStateSaver : Saver<SelectionState, Bundle>{

    override fun restore(value: Bundle): SelectionState {
        return SelectionState().apply {
            setEnabled(value.getBoolean(EXTRA_ENABLED,false))
            selectedItems.addAll((value.getStringArray(EXTRA_ITEMS) ?: emptyArray()))
        }
    }

    override fun SaverScope.save(value: SelectionState): Bundle? {
       return bundleOf(
           EXTRA_ENABLED to value.isEnabled,
           EXTRA_ITEMS to value.selectedItems.toTypedArray()
       )
    }

    private companion object{
        const val EXTRA_ENABLED = "EXTRA_ENABLED"
        const val EXTRA_ITEMS = "EXTRA_ENABLED"
    }
}

class SelectionState(
) {
    val selectedItems = mutableStateListOf<String>()

    val isEnabled : State<Boolean>
        get() = _isEnabled

    fun setEnabled(enabled: Boolean){
        _isEnabled.value = enabled
        if (!enabled)
            selectedItems.clear()
    }

    private val _isEnabled  = mutableStateOf(false)


    fun setSelected(item: String, selected : Boolean){
        if (selected) {
            if (item !in selectedItems)
                selectedItems.add(item)
            setEnabled(true)
        } else {
            selectedItems.remove(item)
            setEnabled(selectedItems.isNotEmpty())
        }
    }

    fun toggleSelection(item: String,disableOnEmpty : Boolean = true) {
        setSelected(item, item !in selectedItems)
    }
}