package github.alexzhirkevich.community.ui.widgets.common

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun NavigateBackIconButton(navController: NavController) {

    val isPressed = remember {
       mutableStateOf(false)
    }

    if (isPressed.value) {
        isPressed.value = false
        (LocalContext.current as? Activity)?.onBackPressed()
    }

    IconButton(
        modifier = Modifier
            .padding(end = 10.dp),
        onClick = {
        isPressed.value = true
    }) {
        Icon(
            imageVector = Icons.Rounded.ArrowBack,
            contentDescription = "Back",
        )
    }
}