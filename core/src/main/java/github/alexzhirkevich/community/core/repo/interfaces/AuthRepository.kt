package github.alexzhirkevich.community.core.repo.interfaces

import github.alexzhirkevich.community.core.repo.base.IndependentRepository

interface AuthRepository : IndependentRepository {

    fun setOnline(onlineNow: Boolean, onlineOnExit : Boolean) : Boolean
    fun signOut()
    val isAuthenticated :Boolean
    fun doOnAuthenticated(action : Runnable)
    suspend fun setNotificationToken(token : String)
}