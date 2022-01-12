package github.alexzhirkevich.community.screens.input.profile.data

import androidx.annotation.StringRes

sealed interface EditProfileError {
    object None : EditProfileError
    object Unchecked : EditProfileError
    class Username(@StringRes val error : Int) : EditProfileError
    class Name(@StringRes val error : Int) : EditProfileError
    class Description(@StringRes val  error : Int) : EditProfileError
}