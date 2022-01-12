package github.alexzhirkevich.community.core.repo.imp

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import github.alexzhirkevich.community.core.await
import github.alexzhirkevich.community.core.repo.interfaces.AuthRepository
import github.alexzhirkevich.community.core.repo.interfaces.FirebaseRepository
import github.alexzhirkevich.community.core.repo.interfaces.UsersRepository
import javax.inject.Inject

open class AuthRepositoryImp @Inject constructor(
    private val usersProvider: UsersRepository
    ) : AuthRepository{

    override fun setOnline(onlineNow: Boolean, onlineOnExit: Boolean): Boolean {

        if (!isAuthenticated)
            return  false

        userOnlineReference.setValue(onlineNow)

        if (!onlineOnExit)
            userOnlineReference.onDisconnect().setValue(false)
        else
            userOnlineReference.onDisconnect().cancel()
        return true
    }

    override val isAuthenticated : Boolean
        get() = FirebaseAuth.getInstance().currentUser != null

    override fun signOut() = FirebaseAuth.getInstance().signOut()

    override fun doOnAuthenticated(action: Runnable) =
        FirebaseAuth.getInstance().addAuthStateListener {
            if (it.currentUser!=null){
                action.run()
            }
        }

    override suspend fun setNotificationToken(token: String) {
        FirebaseDatabase.getInstance().reference
            .child(FirebaseRepository.COLLECTION_USERS)
            .child(usersProvider.currentUserId)
            .child(FirebaseRepository.FIELD_NOTIFY_TOKEN)
            .setValue(token)
            .await()
    }

    private val userOnlineReference : DatabaseReference by lazy {
        FirebaseDatabase.getInstance().reference
            .child(FirebaseRepository.COLLECTION_USERS)
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child(FirebaseRepository.FIELD_ONLINE)
    }
}