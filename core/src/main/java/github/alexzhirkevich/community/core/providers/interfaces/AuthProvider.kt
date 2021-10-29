package github.alexzhirkevich.community.core.providers.interfaces

import github.alexzhirkevich.community.core.providers.base.IndependentProvider

interface AuthProvider : IndependentProvider {

    fun setOnline(onlineNow: Boolean, onlineOnExit : Boolean) : Boolean
    fun signOut()
    val isAuthenticated :Boolean
    fun doOnAuthenticated(action : Runnable)
    suspend fun setNotificationToken(token : String)
}