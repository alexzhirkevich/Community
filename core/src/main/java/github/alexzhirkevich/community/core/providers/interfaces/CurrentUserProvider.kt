package github.alexzhirkevich.community.core.providers.interfaces

import github.alexzhirkevich.community.core.providers.base.Provider

interface CurrentUserProvider : Provider {
    val currentUserId : String
}
