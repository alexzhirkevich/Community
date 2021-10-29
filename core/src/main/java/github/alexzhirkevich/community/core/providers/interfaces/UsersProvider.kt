package github.alexzhirkevich.community.core.providers.interfaces

import github.alexzhirkevich.community.core.entities.interfaces.User
import github.alexzhirkevich.community.core.Response
import github.alexzhirkevich.community.core.providers.base.EntityProvider
import github.alexzhirkevich.community.core.providers.base.IndependentProvider
import kotlinx.coroutines.flow.Flow

interface UsersProvider
    : EntityProvider<User>, CurrentUserProvider, IndependentProvider {

    fun getNotificationToken(userId : String) : Flow<Response<String>>

    fun findByPhone(vararg phones : String) : Flow<Collection<Response<User>>>

    fun findByUsername(username : String) : Flow<Response<User>>

    fun findByUsernameNearly(username: String, limit:Int = 30) : Flow<Collection<Response<User>>>

}


