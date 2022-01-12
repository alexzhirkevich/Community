package github.alexzhirkevich.community.core.repo.interfaces

import github.alexzhirkevich.community.core.entities.interfaces.User
import github.alexzhirkevich.community.core.Response
import github.alexzhirkevich.community.core.repo.base.EntityRepository
import github.alexzhirkevich.community.core.repo.base.IndependentRepository
import kotlinx.coroutines.flow.Flow

interface UsersRepository
    : EntityRepository<User>, CurrentUserRepository, IndependentRepository {

    fun getNotificationToken(userId : String) : Flow<Response<String>>

    fun findByPhone(vararg phones : String) : Flow<Collection<Response<User>>>

    fun findByTag(username : String) : Flow<Response<User>>

    fun findByUsernameNearly(username: String, limit:Int = 30) : Flow<Collection<Response<User>>>

    fun getChatsIds(userId: String,limit: Int = 30) : Flow<Collection<Response<String>>>

    fun getChannelIds(userId: String,limit: Int) : Flow<Collection<Response<String>>>
}


