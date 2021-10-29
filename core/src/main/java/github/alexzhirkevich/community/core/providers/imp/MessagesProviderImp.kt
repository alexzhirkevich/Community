package github.alexzhirkevich.community.core.providers.imp

import github.alexzhirkevich.community.core.entities.imp.GroupImpl
import github.alexzhirkevich.community.core.entities.interfaces.Chat
import github.alexzhirkevich.community.core.entities.interfaces.Sendable
import github.alexzhirkevich.community.core.*
import github.alexzhirkevich.community.core.asResponse
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider.Companion.COLLECTION_MESSAGES
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider.Companion.FIELD_TIME
import github.alexzhirkevich.community.core.providers.interfaces.MessagesProvider
import github.alexzhirkevich.community.core.toMessage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ExperimentalCoroutinesApi
class MessagesProviderImp @Inject constructor(
        private val firebaseProvider: FirebaseProvider
) : MessagesProvider {

    override fun get(id: String, collectionID: String): Flow<Response<Sendable>> =
        firebaseProvider.chatsCollection.document(collectionID).collection(COLLECTION_MESSAGES)
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
        val doc = firebaseProvider.chatsCollection
            .document(entity.chatId).collection(COLLECTION_MESSAGES)
            .document()
        entity.id = doc.id
        doc.set(entity).await()
    }

    override suspend fun delete(entity: Sendable) =
        remove(entity.id, entity.chatId)

    override suspend fun remove(id: String, collection: String) {
        firebaseProvider.chatsCollection
            .document(collection).collection(COLLECTION_MESSAGES)
            .document(id).delete()
            .await()
    }

    override fun getAll(collection: String, limit: Int): Flow<Collection<Response<Sendable>>> =
        flow { }


    override fun last(chatId: String): Flow<Response<Sendable>> =
        firebaseProvider.chatsCollection
            .document(chatId).collection(COLLECTION_MESSAGES)
            .orderBy(FIELD_TIME).limitToLast(1)
            .asResponse {
                it.toMessage()!!
            }.map {
                if (it.isNotEmpty())
                    it.first()
                else
                    Response.Error(SnapshotNotFoundException())
            }
}
