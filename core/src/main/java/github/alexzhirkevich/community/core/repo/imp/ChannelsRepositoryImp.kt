package github.alexzhirkevich.community.core.repo.imp

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import github.alexzhirkevich.community.core.entities.imp.Admin
import github.alexzhirkevich.community.core.entities.interfaces.Channel
import github.alexzhirkevich.community.core.*
import github.alexzhirkevich.community.core.asFlow
import github.alexzhirkevich.community.core.asResponse
import github.alexzhirkevich.community.core.entities.interfaces.Sendable
import github.alexzhirkevich.community.core.repo.data.PostData
import github.alexzhirkevich.community.core.repo.interfaces.*
import github.alexzhirkevich.community.core.repo.interfaces.FirebaseRepository.Companion.COLLECTION_ADMINS
import github.alexzhirkevich.community.core.repo.interfaces.FirebaseRepository.Companion.COLLECTION_CHANNELS
import github.alexzhirkevich.community.core.repo.interfaces.FirebaseRepository.Companion.FIELD_SEARCH_NAME
import github.alexzhirkevich.community.core.repo.interfaces.FirebaseRepository.Companion.FIELD_SEARCH_TAG
import github.alexzhirkevich.community.core.repo.interfaces.FirebaseRepository.Companion.FIELD_SUBCRIBERS
import github.alexzhirkevich.community.core.repo.interfaces.FirebaseRepository.Companion.FIELD_TAG
import github.alexzhirkevich.community.core.repo.interfaces.FirebaseRepository.Companion.LINK_CHANNEL
import github.alexzhirkevich.community.core.repo.interfaces.FirebaseRepository.Companion.PRIVATE
import github.alexzhirkevich.community.core.repo.interfaces.FirebaseRepository.Companion.PUBLIC
import github.alexzhirkevich.community.core.repo.interfaces.FirebaseRepository.Companion.URL_BASE
import github.alexzhirkevich.community.core.toChannel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@FlowPreview
@Singleton
class ChannelsRepositoryImp @Inject constructor(
    private val usersProvider: UsersRepository
) : ChannelsRepository {

    private val channelsCollection = FirebaseFirestore.getInstance()
        .collection(COLLECTION_CHANNELS)

    override val postsRepository: PostsRepository =
        PostsRepositoryImp()

    override fun getAdmins(channelId: String): Flow<Collection<Response<Admin>>> =
        channelsCollection.document(channelId).collection(COLLECTION_ADMINS)
            .asFlow { it.asResponse() }

    override fun getSubscribersCount(channelId: String): Flow<Response<Long>> =
        channelsCollection.document(channelId)
            .collection(PRIVATE).document(PUBLIC).asResponse {
                it[FIELD_SUBCRIBERS].toString().toLong()
            }

    override fun findByTag(tag: String): Flow<Response<Channel>> =
        channelsCollection.whereEqualTo(FIELD_TAG, tag)
            .asResponse { map : Map<String,*> -> map.toChannel() }.map {
                if (it.isNotEmpty())
                    it.first()
                else Response.Error(SnapshotNotFoundException(clazz = Channel::class.java))
            }

    override fun get(id: String): Flow<Response<Channel>> =

        channelsCollection.document(id)
            .asResponse {
                it.toChannel()
            }


    override fun getAll(collection: String, limit: Int): Flow<Collection<Response<Channel>>> {

        return usersProvider.getChannelIds(collection,limit)
            .flatMapMerge {
                it.map {
                    if (it is Response.Success)
                        get(it.value)
                    else flowOf(it as Response.Error)
                }.combine()
            }


    }

    override suspend fun create(entity: Channel) {

        val doc = channelsCollection.document()
        val uId = usersProvider.currentUserId

        entity.id = doc.id
        entity.creatorId = uId
        entity.creationTime = System.currentTimeMillis()

        val map = Channel::class.java.fields.mapNotNull {
            if (it.name != "subscribers")
                it.name to it.get(entity)
            else null
        }.toMap()

        doc.set(entity).await()

//        val userCompletable = (doc.collection(USERS).document(uId)
//                    .set(mapOf(Pair(REFERENCE,usersCollection.document(uId))))).toCompletable()
//
//        val adminCompletable = doc.collection(ADMINS).document(uId).set(
//                    ChannelAdmin(canPost = true, canDelete = true, canBan = true, canEdit = true)).toCompletable()
//
//        val profileCompletable = usersCollection.document(uId).collection(CHANNELS).document(entity.id)
//                    .set(mapOf(Pair(REFERENCE, channelsCollection.document(entity.id)))).toCompletable()
//
//        return Completable.mergeArray(creationCompletable, userCompletable, adminCompletable, profileCompletable)
    }

    override suspend fun delete(entity: Channel) {

        if (entity.creatorId == usersProvider.currentUserId) {
            channelsCollection.document(entity.id).delete().await()
        } else return remove(entity.id, usersProvider.currentUserId)
    }

    override suspend fun remove(id: String, collection: String){}

    override suspend fun join(channelId: String): Response<Channel> {
        TODO()
//        val uid = userListProvider.currentUserId
//        val doc = firebaseProvider.channelsCollection.document(channelId).collection(COLLECTION_USERS).document(uid)
//
//        val check1 = Completable.create {
//            getUsers(uid).singleOrError()
//                    .subscribe(
//                            { list ->
//                                list.find { user -> user.id == channelId }?.let { _ -> it.onComplete() }
//                                        ?: it.tryOnError(InterruptingThrowable)
//                            },
//                            {})
//        }
//
//        var channel: Channel? = null
//
//        val check2 = Completable.create {
//            doc.get().addOnSuccessListener { ds ->
//                if (ds.exists()) {
//                    channel = ds.toObject(Channel::class.java)
//                    it.onComplete()
//                } else {
//                    it.tryOnError(InterruptingThrowable)
//                }
//            }.addOnFailureListener { _ ->
//                it.tryOnError(InterruptingThrowable)
//            }
//        }
//
//        val joinCompletable = doc.set(mapOf(Pair(FIELD_REFERENCE, firebaseProvider.usersCollection.document(uid))))
//                .toCompletable()
//
//        return Single.create {
//            Completable.mergeArray(check1, check2)
//                    .subscribe(
//                            {
//                                channel?.let { it1 -> it.onSuccess(it1) }
//                            },
//                            { ex ->
//                                if (ex is InterruptingThrowable) {
//                                    joinCompletable.subscribe(
//                                            { channel?.let { c -> it.onSuccess(c) } ?: it.tryOnError(NullPointerException())},
//                                            { t -> it.tryOnError(t) })
//                                } else {
//                                    it.tryOnError(ex)
//                                }
//                            }
//                    )
//        }
    }

    override fun find(namePart: String, limit: Int): Flow<Collection<Response<Channel>>> =
        listOf(
            channelsCollection
                .whereGreaterThanOrEqualTo(FIELD_SEARCH_NAME, namePart)
                .whereLessThanOrEqualTo(FIELD_SEARCH_NAME, "$namePart\uF7FF")

                .apply {
                    if (limit > 2)
                        limit(limit / 2.toLong())
                    else if (limit > 1)
                        limit(limit.toLong())
                }.asResponse { map : Map<String,*> -> map.toChannel() },
            channelsCollection
                .whereGreaterThanOrEqualTo(FIELD_SEARCH_TAG, namePart)

                .apply {
                    if (limit > 2)
                        limit(limit / 2.toLong())
                    else if (limit > 1)
                        limit(limit.toLong())
                }.asResponse { map : Map<String,*> -> map.toChannel() },
        ).merge()

    override fun createInviteLink(id: String): String {
        return buildString {
            append(URL_BASE)
            append(LINK_CHANNEL)
            append('/')
            append(id)
        }
    }

    inner class PostsRepositoryImp constructor(): PostsRepository {

        override suspend fun delete(entity: Sendable) {
            channelsCollection.document(entity.chatId)
                .collection(FirebaseRepository.COLLECTION_POSTS).document(entity.id)
                .delete().await()
        }

        override suspend fun remove(id: String, collection: String) {
            channelsCollection.document(collection)
                .collection(FirebaseRepository.COLLECTION_POSTS).document(id)
                .delete().await()
        }

        override suspend fun create(entity: Sendable) {

            val doc = channelsCollection.document(entity.chatId)
                .collection(FirebaseRepository.COLLECTION_POSTS).document()

            entity.id = doc.id

            doc.set(entity).await()
        }

//        override fun lastViewed(channelId: String): Flow<Response<String>> =
//
//                .document(channelId)
//                .asResponse { it[FirebaseRepository.FIELD_LAST_VIEWED_POST]?.toString().orEmpty() }

        override fun getPostData(id: String,channelId: String): Flow<Response<PostData>> =
            channelsCollection.document(channelId).collection(FirebaseRepository.COLLECTION_POSTS)
                .document(id).collection(FirebaseRepository.PRIVATE).document(PUBLIC)
                .asResponse {
                    PostData(
                        id = id,
                        viewsCount = it[FirebaseRepository.FIELD_POST_VIEWS]?.toString()?.toLongOrNull() ?: 0L,
                        repostsCount = it[FirebaseRepository.FIELD_POST_REPOSTS]?.toString()?.toLongOrNull() ?: 0L
                    )
                }


//    override fun last(channelId: String): Observable<out IPost> = Observable.create {
//        firebaseProvider.channelsCollection.document(channelId).collection(COLLECTION_POSTS)
//            .orderBy(FIELD_TIME).limitToLast(1)
//                .toObservable(Post::class.java)
//                .map { list -> list.first() }
//    }

        override fun get(id: String, collectionID: String): Flow<Response<Sendable>> =
            channelsCollection.document(collectionID)
                .collection(FirebaseRepository.COLLECTION_POSTS).document(id)
                .asResponse {
                    it.toPost() ?: throw SnapshotNotFoundException(
                        clazz = Sendable::class.java,
                        id = id,
                        collectionId = collectionID
                    )
                }

        override fun getAll(collection: String, limit: Int) : Flow<Collection<Response<Sendable>>> {
            val col = channelsCollection.document(collection)
                .collection(FirebaseRepository.COLLECTION_POSTS).orderBy(FirebaseRepository.FIELD_TIME)

            val query = if (limit > -1) col.limitToLast(limit.toLong()) else col

            return query.asResponse { it : Map<String,*> ->
                it.toPost() ?: throw SnapshotNotFoundException(
                    clazz = Sendable::class.java,
                    collectionId = collection
                )
            }
        }

    }
}

