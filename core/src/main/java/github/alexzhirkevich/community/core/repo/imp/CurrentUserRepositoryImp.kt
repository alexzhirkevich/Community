package github.alexzhirkevich.community.core.repo.imp

import github.alexzhirkevich.community.core.repo.interfaces.CurrentUserRepository
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrentUserRepositoryImp @Inject constructor()
    : CurrentUserRepository {
    override val currentUserId: String
        get() = FirebaseAuth.getInstance().currentUser?.uid!!

}