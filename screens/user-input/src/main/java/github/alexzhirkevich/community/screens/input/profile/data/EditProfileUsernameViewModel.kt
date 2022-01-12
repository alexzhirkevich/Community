package github.alexzhirkevich.community.screens.input.profile.data

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import github.alexzhirkevich.community.common.DataState
import github.alexzhirkevich.community.common.util.LTAG
import github.alexzhirkevich.community.core.Config
import github.alexzhirkevich.community.core.Response
import github.alexzhirkevich.community.core.SnapshotNotFoundException
import github.alexzhirkevich.community.core.di.Stage
import github.alexzhirkevich.community.core.entities.interfaces.User
import github.alexzhirkevich.community.core.repo.interfaces.ChannelsRepository
import github.alexzhirkevich.community.core.repo.interfaces.UserProfileRepository
import github.alexzhirkevich.community.core.repo.interfaces.UsersRepository
import github.alexzhirkevich.community.screens.input.R
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
@HiltViewModel
class EditProfileUsernameViewModel @Inject constructor(
    @Stage usersRepo : UsersRepository,
    @Stage private val userProfileRepo : UserProfileRepository,
    @Stage private val channelsRepo: ChannelsRepository,
) : EditProfileViewModel(usersRepo){

    @Volatile private var posted = false

    override val sourceFlow: Flow<DataState<User>> =
        super.sourceFlow.onEach {
            if (!posted && it is DataState.Success && text.value.isEmpty()){
                posted = true
                mText.tryEmit(it.value.name)
            }
        }

    override fun setText(text: String) = synchronized(mText) {
        if (text.length in 0..Config.TagMaxLength)
            mText.tryEmit(text)
    }

    override fun applyChanges(onSuccess: () -> Unit) = synchronized(mText) {
        if (error.value is EditProfileError.None)
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    userProfileRepo.setName(usersRepo.currentUserId, text.value)
                    onSuccess()
                } catch (t: CancellationException) {
                } catch (t: Throwable) {
                    logger?.log(
                        "Failed to set profile username",
                        tag = javaClass.simpleName,
                        cause = t
                    )
                }
            }
    }

    override fun update() {
        super.update()
        usernameJob?.cancel()
        usernameJob = runUsernameCheckJob()
    }

    private var usernameJob: Job? = null

    private fun isCorrectUsername(username: String): Boolean =
                username.all { it in Config.TagSymbols } &&
                username.length in Config.TagMinLength..Config.TagMaxLength

    private fun runUsernameCheckJob() : Job {
        mError.tryEmit(EditProfileError.Unchecked)
        return text
            .onEach {
                mError.tryEmit(EditProfileError.Unchecked)
            }
            .debounce { 500 }
            .flatMapLatest {
                if (!isCorrectUsername(it) && it.isNotEmpty())
                    return@flatMapLatest flowOf(false)
                if (it.isEmpty())
                    return@flatMapLatest flowOf(true)
                usersRepo
                    .findByTag(it)
                    .combine(channelsRepo.findByTag(it)) { a, b ->
                        (a as? Response.Success)?.value?.toString()?.let {
                            Log.i(LTAG, it)
                        }
                        a is Response.Success && a.value.id == usersRepo.currentUserId ||
                                a is Response.Error && a.error is SnapshotNotFoundException &&
                                b is Response.Error && b.error is SnapshotNotFoundException
                    }.onEach {
                        val error =  if (it)
                            EditProfileError.None
                        else EditProfileError.Username(R.string.error_usernaname_taken)

                        mError.tryEmit(error)

                    }.catch { t ->
                        logger?.log(
                            msg = "Failed to check username availability",
                            tag = this@EditProfileUsernameViewModel.LTAG,
                            cause = t)

                        mError.tryEmit(EditProfileError.Unchecked)
                    }
            }.launchIn(viewModelScope + Dispatchers.IO)

    }
}
