package github.alexzhirkevich.community.screens.input.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notes
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Tag
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import github.alexzhirkevich.community.core.Config
import github.alexzhirkevich.community.routing.Route
import github.alexzhirkevich.community.routing.popToRoute
import github.alexzhirkevich.community.screens.input.*
import github.alexzhirkevich.community.screens.input.profile.data.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
@Composable
fun EditProfileScreen(
    navController: NavController,
    target : Route.EditProfileScreen.Target,
    editProfileViewModel: EditProfileViewModel = initializeViewModel(target)
) {

    val error by editProfileViewModel.error.collectAsState()
    val text by editProfileViewModel.text.collectAsState()

    UserInputScreen(
        isSubmitEnabled = error == EditProfileError.None,
        title = stringResource(id = R.string.profile),
        onSubmit = {
            editProfileViewModel.applyChanges {
                navController.popToRoute(
                    Route.BottomNavigationScreen,
                    false
                )
            }
        }
    ) {
        val setText = remember {{ it : String -> editProfileViewModel.setText(it) }}
        val modifier = remember {
            Modifier
                .onFocusChanged {
                    editProfileViewModel.setFocused(it.isFocused)
                }
                .fillMaxWidth()
        }

        Box(
            modifier = Modifier
                .padding(
                    start = 15.dp,
                    end = 15.dp,
                    top = 15.dp
                )
                .fillMaxWidth()
        ){
            when (target) {
                is Route.EditProfileScreen.Target.Name -> {
                    NameTextField(
                        text = text,
                        error = error,
                        onChange = setText,
                        modifier = modifier
                    )
                }
                is Route.EditProfileScreen.Target.Username -> {
                    UsernameTextField(
                        text = text,
                        error = error,
                        onChange = setText,
                        modifier = modifier
                    )
                }
                is Route.EditProfileScreen.Target.Description -> {
                    DescriptionTextField(
                        text = text,
                        error = error,
                        onChange = setText,
                        modifier = modifier
                    )
                }
                else -> {
                    // should never happen
                }
            }
        }
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
@Composable
private fun initializeViewModel(target: Route.EditProfileScreen.Target) : EditProfileViewModel {
    return when (target){
        Route.EditProfileScreen.Target.Name ->
            hiltViewModel<EditProfileNameViewModel>()
        Route.EditProfileScreen.Target.Username ->
            hiltViewModel<EditProfileUsernameViewModel>()
        Route.EditProfileScreen.Target.Description ->
            hiltViewModel<EditProfileDescriptionViewModel>()
        else -> throw IllegalArgumentException("Unexpected edit profile target")
    }
}


@Composable
private fun EditProfileTextField(
    text : String,
    error : EditProfileError = EditProfileError.None,
    onChange : (String) -> Unit,
    keyboardOptions: KeyboardOptions,
    label : String,
    leadingIcon : ImageVector
    ) {

    val isError = error !is EditProfileError.None

    TextField(
        value = text,
        maxLines = 1,
        onValueChange = onChange,
        keyboardOptions = keyboardOptions,
        label = {
            LabelText(text = label)
        },
        trailingIcon = {
            if (isError) {
                ErrorTrailingIcon()
            }
        },
        leadingIcon = {
            LeadingIcon(
                icon = leadingIcon,
                tint =  MaterialTheme.colors.secondaryVariant
            )
        },
        isError = isError,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            cursorColor = LocalTextStyle.current.color,
            focusedIndicatorColor = MaterialTheme.colors.surface
        )
    )
}


@Composable
private fun NameTextField(
    text : String,
    modifier: Modifier = Modifier,
    error : EditProfileError = EditProfileError.None,
    onChange : (String) -> Unit,
) {

    val isError = error !is EditProfileError.None

    Column(modifier) {
        EditProfileTextField(
            text, error, onChange,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                autoCorrect = false,
            ),
            label = stringResource(id = R.string.name),
            leadingIcon = Icons.Rounded.Person
        )

        AssistedText(
            text = if (error is EditProfileError.Name)
                stringResource(id = error.error)
            else stringResource(id = R.string.assist_name, Config.NameMinLength),
            isError = isError,
            symbolsCounter = text.length to Config.NameMaxLength
        )
    }
}

@Composable
private fun UsernameTextField(
    text : String,
    modifier: Modifier = Modifier,
    error : EditProfileError = EditProfileError.None,
    onChange : (String) -> Unit
) {

    val isError = error !is EditProfileError.None

    Column(modifier) {
        EditProfileTextField(
            text, error, onChange,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
            ),
            label = stringResource(id = R.string.username_optional),
            leadingIcon = Icons.Rounded.Tag
        )

        AssistedText(
            text = if (error is EditProfileError.Username)
                stringResource(id = error.error)
            else stringResource(
                id = R.string.assist_username,
                Config.TagMinLength
            ),
            isError = isError,
            symbolsCounter = text.length to Config.TagMaxLength
        )
    }
}

@Composable
private fun DescriptionTextField(
    text : String,
    modifier: Modifier = Modifier,
    error : EditProfileError = EditProfileError.None,
    onChange : (String) -> Unit
) {
    val isError = error !is EditProfileError.None

    Column(modifier) {
        EditProfileTextField(
            text, error, onChange,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                autoCorrect = true,
            ),
            label = stringResource(id = R.string.description_optional),
            leadingIcon = Icons.Rounded.Notes
        )

        AssistedText(
            text = if (error is EditProfileError.Description)
                stringResource(id = error.error)
            else stringResource(id = R.string.assist_description),
            isError = isError,
            symbolsCounter = text.length to Config.DescriptionMaxLength
        )
    }
}
