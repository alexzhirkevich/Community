package github.alexzhirkevich.community.core.repo.interfaces

import github.alexzhirkevich.community.core.repo.base.IndependentRepository
import github.alexzhirkevich.community.core.repo.base.Repository

interface CurrentUserRepository : Repository, IndependentRepository {
    val currentUserId : String
}
