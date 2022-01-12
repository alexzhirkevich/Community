package github.alexzhirkevich.community.screens.auth.data

import dagger.hilt.android.lifecycle.HiltViewModel
import github.alexzhirkevich.community.common.DataState
import github.alexzhirkevich.community.common.DataViewModel
import github.alexzhirkevich.community.core.entities.interfaces.User
import github.alexzhirkevich.community.core.repo.interfaces.PhoneAuthRepository
import github.alexzhirkevich.community.screens.auth.di.DaggerAuthComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoggedInUser(
    val value : User,
    val isNew : Boolean
)

@ExperimentalCoroutinesApi
@FlowPreview
@ExperimentalStdlibApi
@HiltViewModel
class AuthViewModel @Inject constructor(val phoneAuthRepository: PhoneAuthRepository) : DataViewModel<DataState<LoggedInUser>>() {


    override val sourceFlow: Flow<DataState<DataState<LoggedInUser>>>
        get() = TODO("Not yet implemented")
}