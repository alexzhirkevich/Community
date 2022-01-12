package github.alexzhirkevich.community.common.composable

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import github.alexzhirkevich.community.theme.Dimens

@Composable
fun NavigateBackIconButton(
    modifier: Modifier = Modifier
) {

    val isPressed = remember {
        mutableStateOf(false)
    }

    if (isPressed.value) {
        isPressed.value = false
        (LocalContext.current as? Activity)?.onBackPressed()
    }

    IconButton(
        modifier = modifier
            .padding(horizontal = 15.dp)
            .size(Dimens.ToolbarBtnSize),
        onClick = {
            isPressed.value = true
        }) {
        Icon(
            imageVector = Icons.Rounded.ArrowBack,
            tint = MaterialTheme.colors.surface,
            contentDescription = "Back",
        )
    }
    Spacer(modifier = Modifier.width(5.dp))

}