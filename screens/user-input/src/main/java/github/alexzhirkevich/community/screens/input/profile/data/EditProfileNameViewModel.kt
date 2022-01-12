package github.alexzhirkevich.community.screens.input.profile.data

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import github.alexzhirkevich.community.common.DataState
import github.alexzhirkevich.community.core.Config
import github.alexzhirkevich.community.core.di.Stage
import github.alexzhirkevich.community.core.entities.interfaces.User
import github.alexzhirkevich.community.core.repo.interfaces.UserProfileRepository
import github.alexzhirkevich.community.core.repo.interfaces.UsersRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileNameViewModel @Inject constructor(
    @Stage usersRepo : UsersRepository,
    @Stage private val userProfileRepo : UserProfileRepository,
) : EditProfileViewModel(usersRepo) {

    @Volatile private var posted = false

    override val sourceFlow: Flow<DataState<User>> =
        super.sourceFlow.onEach {
            if (!posted && it is DataState.Success && text.value.isEmpty()){
                posted = true
                mText.tryEmit(it.value.name)
            }
        }

    override fun setText(text: String) = synchronized(mText){
        if (text.length in 0..Config.NameMaxLength)
            mText.tryEmit(text)
    }

    override fun applyChanges(onSuccess: () -> Unit) = synchronized(mText){
        if (error.value is EditProfileError.None) {
            try {
                viewModelScope.launch(Dispatchers.IO) {
                    userProfileRepo.setName(usersRepo.currentUserId, text.value)
                    onSuccess()
                }
            } catch (t: CancellationException) {
            } catch (t: Throwable) {
                logger?.log(
                    "Failed to set profile name",
                    tag = javaClass.simpleName,
                    cause = t
                )
            }
        }
    }
}