package github.alexzhirkevich.community.core.repo.imp

import com.google.firebase.firestore.FirebaseFirestore
import github.alexzhirkevich.community.core.entities.interfaces.Chat
import github.alexzhirkevich.community.core.*
import github.alexzhirkevich.community.core.asResponse
import github.alexzhirkevich.community.core.entities.interfaces.Sendable
import github.alexzhirkevich.community.core.repo.data.PostData
import github.alexzhirkevich.community.core.repo.interfaces.*
import github.alexzhirkevich.community.core.repo.interfaces.FirebaseRepository.Companion.COLLECTION_CHATS
import github.alexzhirkevich.community.core.toChat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@FlowPreview
@ExperimentalCoroutinesApi
@Singleton
class ChatsRepositoryImp @Inject constructor(
    private val usersRepo: UsersRepository
) : ChatsRepository {

    private val chatsCollection = FirebaseFirestore.getInstance()
        .collection(COLLECTION_CHATS)


    override val messagesRepository: MessagesRepository =
        MessagesRepositoryImp()

    override fun getAll(collection: String, limit: Int): Flow<Collection<Response<Chat>>> {
        val col = usersRepo.getChatsIds(collection, limit)
            .flatMapMerge {
                it.map { id ->
                    if (id is Response.Success)
                        get(id.value)
                    else flowOf(id as Response.Error)
                }.combine()
            }
        return col
    }


    override suspend fun invite(chatId: String, userId: String) {
        TODO("Not yet implemented")
    }

    override fun get(id: String): Flow<Response<Chat>> =
        chatsCollection.document(id).asResponse {
            it.toChat() ?: throw EntityNotFoundException(Chat::class.java, id = id)
        }


    override suspend fun create(entity: Chat) {
        val doc = chatsCollection.document()
        entity.id = doc.id
        doc.set(entity).await()
    }

    override suspend fun delete(entity: Chat) {
        chatsCollection.document(entity.id).delete().await()
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
    inner class MessagesRepositoryImp : MessagesRepository {

        override fun get(id: String, collectionID: String): Flow<Response<Sendable>> =
            chatsCollection.document(collectionID)
                .collection(FirebaseRepository.COLLECTION_MESSAGES)
                .document(id).asResponse {
                    it.toMessage() ?: throw SnapshotNotFoundException(
                        clazz = Sendable::class.java,
                        id = id,
                        collectionId = collectionID
                    )
                }


        override suspend fun create(entity: Sendable) {
            if (entity.chatId.isEmpty()) {
                throw Exception("Chat for message not found")
            }
            val doc = chatsCollection
                .document(entity.chatId).collection(FirebaseRepository.COLLECTION_MESSAGES)
                .document()
            entity.id = doc.id
            doc.set(entity).await()
        }

        override suspend fun delete(entity: Sendable) =
            remove(entity.id, entity.chatId)

        override suspend fun remove(id: String, collection: String) {
            chatsCollection
                .document(collection).collection(FirebaseRepository.COLLECTION_MESSAGES)
                .document(id).delete()
                .await()
        }

        override fun getAll(collection: String, limit: Int): Flow<Collection<Response<Sendable>>> =
            flow { }


        override fun last(chatId: String): Flow<Response<Sendable>> =
            chatsCollection
                .document(chatId).collection(FirebaseRepository.COLLECTION_MESSAGES)
                .orderBy(FirebaseRepository.FIELD_TIME).limitToLast(1)
                .asResponse { it: Map<String, *> ->
                    it.toMessage()!!
                }.map {
                    if (it.isNotEmpty())
                        it.first()
                    else
                        Response.Error(SnapshotNotFoundException())
                }
    }
}
