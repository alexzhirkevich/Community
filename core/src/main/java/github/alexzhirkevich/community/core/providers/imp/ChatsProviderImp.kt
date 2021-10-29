package github.alexzhirkevich.community.core.providers.imp

import github.alexzhirkevich.community.core.entities.interfaces.User
import github.alexzhirkevich.community.core.entities.interfaces.Chat
import github.alexzhirkevich.community.core.*
import github.alexzhirkevich.community.core.asResponse
import github.alexzhirkevich.community.core.providers.interfaces.ChatsProvider
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider.Companion.COLLECTION_CHATS
import github.alexzhirkevich.community.core.providers.interfaces.UsersProvider
import github.alexzhirkevich.community.core.toChat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import javax.inject.Inject
import javax.inject.Singleton

@FlowPreview
@ExperimentalCoroutinesApi
@Singleton
class ChatsProviderImp @Inject constructor(
        private val userListProvider: UsersProvider,
        private val firebaseProvider: FirebaseProvider

) : ChatsProvider {


    override fun getAll(collection: String, limit: Int): Flow<Collection<Response<Chat>>> {
        val col = firebaseProvider.usersCollection
            .document(collection)
            .collection(COLLECTION_CHATS)
            .asFlow { it.id }
            .flatMapMerge {
                it.map { id -> get(id) }.combine()
            }

        return col
    }


    override suspend fun invite(chatId: String, userId: String)  {
        TODO("Not yet implemented")
    }

    override fun get(id: String): Flow<Response<Chat>> =
            firebaseProvider.chatsCollection.document(id).asResponse {
                it.toChat() ?: throw EntityNotFoundException(Chat::class.java, id =id)
            }


    override suspend fun create(entity: Chat) {
        val doc = firebaseProvider.chatsCollection.document()
        entity.id = doc.id
        doc.set(entity).await()
    }

    override suspend fun delete(entity: Chat) {
        firebaseProvider.chatsCollection.document(entity.id).delete().await()
    }

    override suspend fun remove(id: String, collection: String) {

    }

//    override fun invite(chatId: String): Maybe<out IChat> {
//
//        val uid = userListProvider.currentUserId
//        val addUserCompletable = firebaseProvider.chatsCollection.document(chatId).collection(COLLECTION_USERS).document(uid)
//                .set(mapOf(Pair(FIELD_REFERENCE, firebaseProvider.usersCollection.document(uid))), SetOptions.merge())
//                .toCompletable()
//
//
////        return userListProvider.joinChat(chatId)
////                .andThen(addUserCompletable)
////                .andThen(get(chatId).singleElement())
//        return Maybe.error(NotImplementedError())
//    }

}

