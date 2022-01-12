package github.alexzhirkevich.community.core.repo.stage

import github.alexzhirkevich.community.core.repo.interfaces.UserProfileRepository
import javax.inject.Inject

class TestUserProfileRepository @Inject constructor()
    : UserProfileRepository {
    override suspend fun setName(id: String, name: String) {

    }

    override suspend fun setUsername(id: String, username: String) {
    }

    override suspend fun setDescription(id: String, text: String) {
    }

    override suspend fun setImageUri(id: String, uri: String) {
    }
}