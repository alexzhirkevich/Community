package github.alexzhirkevich.community.ui.widgets.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Attachment
import androidx.compose.material.icons.rounded.EmojiEmotions
import androidx.compose.material.icons.rounded.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import github.alexzhirkevich.community.ui.theme.Colors
import github.alexzhirkevich.community.ui.theme.Dimens
import github.alexzhirkevich.community.ui.theme.ifInDarkMode

@Composable
fun MessageInput(){

    var inputState by remember { mutableStateOf(TextFieldValue("")) }

    Row(
        modifier = Modifier.background(color = Colors.DarkGray),
        verticalAlignment = Alignment.Bottom
    ) {
        IconButton(
            onClick = { }
        ) {
            Icon(
                imageVector = Icons.Rounded.EmojiEmotions,
                contentDescription = "Emoji",
                tint = Color.Yellow
            )
        }

        TextField(
            modifier = Modifier
                .weight(1f),
            value = inputState,
            onValueChange = { inputState = it },
            maxLines = 5,

            placeholder = {
                Text(" Message",color = Colors.Gray)
            },
            colors = TextFieldDefaults.textFieldColors(
                disabledTextColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                backgroundColor = Color.Transparent
            )
        )
        IconButton(
            onClick = { }
        ) {
            Icon(
                imageVector = Icons.Rounded.Attachment,
                contentDescription = "Attach",
                tint = ifInDarkMode(t = Colors.Gray, f = Colors.Blue)
            )
        }
        IconButton(
            onClick = { }
        ) {
            Icon(
                imageVector = Icons.Rounded.Send,
                contentDescription = "Send",
                tint = ifInDarkMode(t = Colors.Gray, f = Colors.Blue)
            )
        }
    }
}