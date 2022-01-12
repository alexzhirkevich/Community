package github.alexzhirkevich.community.core.repo.stage

import github.alexzhirkevich.community.core.Response
import github.alexzhirkevich.community.core.SnapshotNotFoundException
import github.alexzhirkevich.community.core.entities.imp.UserImpl
import github.alexzhirkevich.community.core.entities.interfaces.User
import github.alexzhirkevich.community.core.repo.interfaces.CurrentUserRepository
import github.alexzhirkevich.community.core.repo.interfaces.UsersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class TestUsersRepository @Inject constructor() : UsersRepository {

    private val currentUserRepository: CurrentUserRepository=
        TestCurrentUserRepository()

    override val currentUserId: String
        get() = currentUserRepository.currentUserId

    override fun getNotificationToken(userId: String): Flow<Response<String>> {
        TODO("Not yet implemented")
    }

    override fun findByPhone(vararg phones: String): Flow<Collection<Response<User>>> {
        TODO("Not yet implemented")
    }

    override fun findByTag(username: String): Flow<Response<User>> {
        return when (username) {
            "username" -> return get(currentUserId)
            "taken" -> get("taken")
            else -> flowOf(Response.Error(SnapshotNotFoundException()))
        }
    }

    override fun findByUsernameNearly(
        username: String,
        limit: Int
    ): Flow<Collection<Response<User>>> {
        TODO("Not yet implemented")
    }

    override fun getChatsIds(userId: String, limit: Int): Flow<Collection<Response<String>>> =
        flowOf(List(limit) {
            Response.Success(it.toString(), false, false)
        })

    override fun getChannelIds(userId: String, limit: Int): Flow<Collection<Response<String>>> =
        flowOf(List(limit) {
            Response.Success(it.toString(), false, false)
        })


    override fun get(id: String): Flow<Response<User>> =
        flowOf(Response.Success(
            UserImpl(
                id = id,
                imageUri = if (id == currentUserId)
                    "https://i.ytimg.com/vi/97gTzS9VFxo/hq720.jpg?sqp=-oaymwEXCNAFEJQDSFryq4qpAwkIARUAAIhCGAE=&rs=AOn4CLAb-iOj1IpkyoFhIWVwx5vsVeM2sA"
                else "",
                tag = "username",
                description = "User description",
                name = "User $id",
                isOnline = true,
            ),false,false
        ))

    override suspend fun create(entity: User) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(entity: User) {
        TODO("Not yet implemented")
    }



}