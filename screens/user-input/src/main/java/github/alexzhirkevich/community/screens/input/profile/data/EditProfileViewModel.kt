package github.alexzhirkevich.community.screens.input.profile.data

import github.alexzhirkevich.community.common.DataState
import github.alexzhirkevich.community.common.DataViewModel
import github.alexzhirkevich.community.common.toDataState
import github.alexzhirkevich.community.core.Response
import github.alexzhirkevich.community.core.di.Stage
import github.alexzhirkevich.community.core.entities.interfaces.User
import github.alexzhirkevich.community.core.repo.interfaces.UsersRepository
import kotlinx.coroutines.flow.*

sealed class EditProfileViewModel(
     @Stage protected val usersRepo : UsersRepository,
) : DataViewModel<User>() {

    override val sourceFlow: Flow<DataState<User>> = usersRepo
        .get(usersRepo.currentUserId)
        .map(Response<User>::toDataState)

    private val _focused = MutableStateFlow(false)
    val focused = _focused.asStateFlow()

    protected val mError = MutableStateFlow<EditProfileError>(EditProfileError.None)
    val error = mError.asStateFlow()

    protected val mText = MutableStateFlow("")
    val text : StateFlow<String> = mText.asStateFlow()

    open fun setText(text : String) {
        mText.tryEmit(text)
    }

    fun setFocused(focused : Boolean){
        _focused.tryEmit(focused)
    }

    abstract fun applyChanges(onSuccess: () -> Unit = {})
}