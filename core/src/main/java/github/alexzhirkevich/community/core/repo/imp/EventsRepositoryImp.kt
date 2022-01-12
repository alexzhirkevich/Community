package github.alexzhirkevich.community.core.repo.imp

import github.alexzhirkevich.community.core.entities.imp.EventImpl
import github.alexzhirkevich.community.core.entities.interfaces.IEvent
import github.alexzhirkevich.community.core.Response
import github.alexzhirkevich.community.core.asResponse
import github.alexzhirkevich.community.core.await
import github.alexzhirkevich.community.core.repo.interfaces.EventsRepository
import github.alexzhirkevich.community.core.repo.interfaces.FirebaseRepository
import github.alexzhirkevich.community.core.repo.interfaces.FirebaseRepository.Companion.COLLECTION_EVENTS
import github.alexzhirkevich.community.core.repo.interfaces.FirebaseRepository.Companion.FIELD_TIME
import github.alexzhirkevich.community.core.repo.interfaces.UsersRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Singleton
class EventsRepositoryImp @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val usersProvider: UsersRepository
) : EventsRepository {

    override fun get(id: String): Flow<Response<IEvent>> =
            firebaseRepository.eventCollection.document(id)
                .asResponse<EventImpl>()

    override suspend fun create(entity: IEvent) {
        val doc = firebaseRepository.eventCollection.document()
        entity.id = doc.id
        entity.creatorId = usersProvider.currentUserId
        doc.set(entity).await()
    }

    override suspend fun delete(entity: IEvent) {
        if (entity.creatorId == usersProvider.currentUserId)
            firebaseRepository.usersCollection.document(usersProvider.currentUserId)
                    .collection(COLLECTION_EVENTS).document(entity.id).delete().await()
        else return remove(entity.id)
    }

    override fun getAll(limit: Int): Flow<Collection<Response<IEvent>>> {
        var query = firebaseRepository.usersCollection.document(usersProvider.currentUserId)
                .collection(COLLECTION_EVENTS).orderBy(FIELD_TIME)
        if (limit > -1)
            query = query.limit(limit.toLong())

        return query.asResponse<EventImpl>()
    }

    override suspend fun remove(id: String) {
        firebaseRepository.usersCollection.document(usersProvider.currentUserId)
            .collection(COLLECTION_EVENTS).document(id).delete().await()
    }
}

