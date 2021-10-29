package github.alexzhirkevich.community.core.providers.imp

import github.alexzhirkevich.community.core.entities.interfaces.Channel
import github.alexzhirkevich.community.core.entities.interfaces.Sendable
import github.alexzhirkevich.community.core.*
import github.alexzhirkevich.community.core.providers.data.PostData
import github.alexzhirkevich.community.core.providers.interfaces.CurrentUserProvider
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider.Companion.COLLECTION_CHANNELS
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider.Companion.COLLECTION_POSTS
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider.Companion.FIELD_LAST_VIEWED_POST
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider.Companion.FIELD_POST_REPOSTS
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider.Companion.FIELD_POST_VIEWS
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider.Companion.FIELD_TIME
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider.Companion.PUBLIC
import github.alexzhirkevich.community.core.providers.interfaces.PostsProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Singleton
class PostsProviderImp @Inject constructor(
        private val firebaseProvider: FirebaseProvider,
        private val currentUserProvider: CurrentUserProvider
): PostsProvider {

    override suspend fun delete(entity: Sendable) {
        firebaseProvider.channelsCollection.document(entity.chatId)
            .collection(COLLECTION_POSTS).document(entity.id)
            .delete().await()
    }

    override suspend fun remove(id: String, collection: String) {
        firebaseProvider.channelsCollection.document(collection)
            .collection(COLLECTION_POSTS).document(id)
            .delete().await()
    }

    override suspend fun create(entity: Sendable) {

        val doc = firebaseProvider.channelsCollection.document(entity.chatId)
            .collection(COLLECTION_POSTS).document()

        entity.id = doc.id

        doc.set(entity).await()
    }

    override fun lastViewed(channelId: String): Flow<Response<String>> =
        firebaseProvider.usersCollection
            .document(currentUserProvider.currentUserId)
            .collection(COLLECTION_CHANNELS)
            .document(channelId)
            .asResponse { it[FIELD_LAST_VIEWED_POST]?.toString().orEmpty() }

    override fun getPostData(id: String,channelId: String): Flow<Response<PostData>> =
        firebaseProvider.channelsCollection.document(channelId).collection(COLLECTION_POSTS)
            .document(id).collection(FirebaseProvider.PRIVATE).document(PUBLIC)
            .asResponse {
                PostData(
                    id = id,
                    viewsCount = it[FIELD_POST_VIEWS]?.toString()?.toLongOrNull() ?: 0L,
                    repostsCount = it[FIELD_POST_REPOSTS]?.toString()?.toLongOrNull() ?: 0L
                )
            }


//    override fun last(channelId: String): Observable<out IPost> = Observable.create {
//        firebaseProvider.channelsCollection.document(channelId).collection(COLLECTION_POSTS)
//            .orderBy(FIELD_TIME).limitToLast(1)
//                .toObservable(Post::class.java)
//                .map { list -> list.first() }
//    }

    override fun get(id: String, collectionID: String): Flow<Response<Sendable>> =
            firebaseProvider.channelsCollection.document(collectionID)
                .collection(COLLECTION_POSTS).document(id)
                    .asResponse {
                        it.toPost() ?: throw SnapshotNotFoundException(
                            clazz = Sendable::class.java,
                            id = id,
                            collectionId = collectionID
                        )
                    }

    override fun getAll(collection: String, limit: Int) : Flow<Collection<Response<Sendable>>> {
        val col = firebaseProvider.channelsCollection.document(collection)
            .collection(COLLECTION_POSTS).orderBy(FIELD_TIME)

        val query = if (limit > -1) col.limitToLast(limit.toLong()) else col

        return query.asResponse {
            it.toPost() ?: throw SnapshotNotFoundException(
                clazz = Sendable::class.java,
                collectionId = collection
            )
        }
    }

}

