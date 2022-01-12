package github.alexzhirkevich.community.core.repo.stage

import github.alexzhirkevich.community.core.repo.interfaces.CurrentUserRepository
import javax.inject.Inject

class TestCurrentUserRepository @Inject constructor(): CurrentUserRepository {
    override val currentUserId: String
        get() = "1233123"
}