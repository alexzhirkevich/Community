package github.alexzhirkevich.community.bottomnavigation.selfprofile.data

import android.content.Context
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import github.alexzhirkevich.community.common.DataState
import github.alexzhirkevich.community.common.DataViewModel
import github.alexzhirkevich.community.common.toDataState
import github.alexzhirkevich.community.common.util.LTAG
import github.alexzhirkevich.community.common.valueOrNull
import github.alexzhirkevich.community.core.Response
import github.alexzhirkevich.community.core.di.Stage
import github.alexzhirkevich.community.core.entities.interfaces.User
import github.alexzhirkevich.community.core.logger.Logger
import github.alexzhirkevich.community.core.repo.interfaces.TaggableRepository
import github.alexzhirkevich.community.core.repo.interfaces.UsersRepository
import github.alexzhirkevich.community.features.aft.TagNavigator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SelfProfileViewModel @Inject constructor (
    @Stage private val usersRepository: UsersRepository,
    @Stage taggableRepository: TaggableRepository,
    logger : Logger
): DataViewModel<User>(logger), TaggableRepository by taggableRepository {

    override val sourceFlow: Flow<DataState<User>> =
        usersRepository.get(usersRepository.currentUserId)
            .map(Response<User>::toDataState)

    init {
        update()
    }
}