package github.alexzhirkevich.community.core.repo.imp

import github.alexzhirkevich.community.core.repo.interfaces.FirebaseRepository
import github.alexzhirkevich.community.core.repo.interfaces.FirebaseRepository.Companion.COLLECTION_CHANNELS
import github.alexzhirkevich.community.core.repo.interfaces.FirebaseRepository.Companion.COLLECTION_CHATS
import github.alexzhirkevich.community.core.repo.interfaces.FirebaseRepository.Companion.COLLECTION_EVENTS
import github.alexzhirkevich.community.core.repo.interfaces.FirebaseRepository.Companion.COLLECTION_USERS
import github.alexzhirkevich.community.core.repo.interfaces.FirebaseRepository.Companion.LINK_CHANNEL
import github.alexzhirkevich.community.core.repo.interfaces.FirebaseRepository.Companion.LINK_CHAT
import github.alexzhirkevich.community.core.repo.interfaces.FirebaseRepository.Companion.URL_BASE
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseRepositoryImp @Inject constructor() : FirebaseRepository {

    override val channelsCollection : CollectionReference
        get() = FirebaseFirestore.getInstance().collection(COLLECTION_CHANNELS)

    override val usersCollection : CollectionReference
        get() = FirebaseFirestore.getInstance().collection(COLLECTION_USERS)

    override val chatsCollection : CollectionReference
        get() = FirebaseFirestore.getInstance().collection(COLLECTION_CHATS)

    override val eventCollection: CollectionReference
        get() = FirebaseFirestore.getInstance().collection(COLLECTION_EVENTS)

    fun createChatInviteLink(id: String): CharSequence =
            buildString {
                append(URL_BASE)
                append(LINK_CHAT)
                append(id)
            }
    fun createChannelInviteLink(id: String): CharSequence =
            buildString {
                append(URL_BASE)
                append(LINK_CHANNEL)
                append(id)
            }
}
