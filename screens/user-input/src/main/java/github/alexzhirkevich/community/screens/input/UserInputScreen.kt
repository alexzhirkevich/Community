package github.alexzhirkevich.community.screens.input

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Error
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import github.alexzhirkevich.community.common.composable.NavigateBackIconButton
import github.alexzhirkevich.community.theme.Colors
import github.alexzhirkevich.community.theme.Dimens

@Composable
internal fun LabelText(text : String) {
    Text(
        text = text,
        style = MaterialTheme.typography.body2
    )
}

@Composable
internal fun ErrorTrailingIcon(){
    Icon(
        imageVector = Icons.Rounded.Error,
        contentDescription = "Error",
    tint = MaterialTheme.colors.error)
}

@Composable fun AssistedText(
    text: String,
    isError : Boolean,
    modifier : Modifier = Modifier,
    symbolsCounter : Pair<Int,Int> ?= null,
){
    Row(
        modifier = modifier.padding(
            top = 5.dp,
            end = 10.dp,
            start = 20.dp
        ),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = text,
            style = MaterialTheme.typography.body2,
            color = if (isError) MaterialTheme.colors.error
            else MaterialTheme.colors.secondaryVariant
        )
        if (symbolsCounter != null) {
            Text(
                modifier = Modifier
                    .padding(start = 20.dp),
                text = "${symbolsCounter.first}/${symbolsCounter.second}",
                style = MaterialTheme.typography.body2,
                color = if (isError) MaterialTheme.colors.error
                else MaterialTheme.colors.secondaryVariant
            )
        }
    }
}

@Composable
internal fun LeadingIcon(icon : ImageVector, tint : Color){
    Icon(
        imageVector = icon,
        contentDescription = "Icon",
        tint = tint,
        modifier = Modifier.size(30.dp)
    )
}

@Composable
internal fun UserInputScreen(
    title : String = "",
    isSubmitEnabled : Boolean = true,
    onSubmit : () -> Unit = {},
    content : @Composable (PaddingValues)->Unit = {},
){
    Scaffold(
        topBar = {
           AppBar(title = title,isSubmitEnabled, onSubmit)
        },content = content,
    )
}

@Composable
private fun AppBar(
    title: String,
    isSubmitEnabled : Boolean,
    onSubmit : () -> Unit
    ) {

    TopAppBar(
        backgroundColor = MaterialTheme.colors.background,
        actions = {
                  IconButton(
                      onClick = onSubmit,
                      enabled = isSubmitEnabled
                  ) {
                     Icon(
                         imageVector = Icons.Rounded.Done,
                         contentDescription = "Save",
                         tint = if (isSubmitEnabled)
                             MaterialTheme.colors.surface
                            else Colors.Gray
                     )
                  }
        },
        title = {
            Text(
                text = title,
                fontSize = Dimens.FontSizeLarge
            )
        },
        navigationIcon = {
            NavigateBackIconButton()
        },
    )
}

