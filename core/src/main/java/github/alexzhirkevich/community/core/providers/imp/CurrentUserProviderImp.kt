package github.alexzhirkevich.community.core.providers.imp

import github.alexzhirkevich.community.core.providers.interfaces.CurrentUserProvider
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrentUserProviderImp @Inject constructor()
    : CurrentUserProvider {
    override val currentUserId: String
        get() = FirebaseAuth.getInstance().currentUser?.uid!!

}