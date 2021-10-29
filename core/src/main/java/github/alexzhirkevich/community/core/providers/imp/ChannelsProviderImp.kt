package github.alexzhirkevich.community.core.providers.imp

import github.alexzhirkevich.community.core.entities.imp.Admin
import github.alexzhirkevich.community.core.entities.imp.UserImpl
import github.alexzhirkevich.community.core.entities.interfaces.User
import github.alexzhirkevich.community.core.entities.interfaces.Channel
import github.alexzhirkevich.community.core.*
import github.alexzhirkevich.community.core.asFlow
import github.alexzhirkevich.community.core.asResponse
import github.alexzhirkevich.community.core.providers.interfaces.ChannelsProvider
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider.Companion.COLLECTION_ADMINS
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider.Companion.COLLECTION_CHANNELS
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider.Companion.FIELD_SEARCH_NAME
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider.Companion.FIELD_SEARCH_TAG
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider.Companion.FIELD_SUBCRIBERS
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider.Companion.FIELD_TAG
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider.Companion.LINK_CHANNEL
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider.Companion.PRIVATE
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider.Companion.PUBLIC
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider.Companion.URL_BASE
import github.alexzhirkevich.community.core.providers.interfaces.UsersProvider
import github.alexzhirkevich.community.core.toChannel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Singleton
class ChannelsProviderImp @Inject constructor(
    private val usersProvider: UsersProvider,
    private val firebaseProvider: FirebaseProvider
) : ChannelsProvider {

    override fun getAdmins(channelId: String): Flow<Collection<Response<Admin>>> =
        firebaseProvider.channelsCollection.document(channelId).collection(COLLECTION_ADMINS)
            .asFlow { it.asResponse() }

    override fun getSubscribersCount(channelId: String): Flow<Response<Long>> =
        firebaseProvider.channelsCollection.document(channelId)
            .collection(PRIVATE).document(PUBLIC).asResponse {
                it[FIELD_SUBCRIBERS].toString().toLong()
            }

    override fun findByTag(tag: String): Flow<Response<Channel>> =
        firebaseProvider.channelsCollection.whereEqualTo(FIELD_TAG, tag)
            .asResponse { it.toChannel() }.map {
                if (it.isNotEmpty())
                    it.first()
                else Response.Error(SnapshotNotFoundException(clazz = Channel::class.java))
            }

    override fun get(id: String): Flow<Response<Channel>> =

        firebaseProvider.channelsCollection.document(id)
            .asResponse {
                it.toChannel()
            }


    @FlowPreview
    override fun getAll(collection: String, limit: Int): Flow<Collection<Response<Channel>>> {

        return firebaseProvider.usersCollection.document(collection)
            .collection(COLLECTION_CHANNELS)
            .asFlow { it.id }
            .flatMapMerge {
                it.map { get(it) }.combine()
            }


    }

    override suspend fun create(entity: Channel) {

        val doc = firebaseProvider.channelsCollection.document()
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
            firebaseProvider.channelsCollection.document(entity.id).delete().await()
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
            firebaseProvider.channelsCollection
                .whereGreaterThanOrEqualTo(FIELD_SEARCH_NAME, namePart)
                .whereLessThanOrEqualTo(FIELD_SEARCH_NAME, "$namePart\uF7FF")

                .apply {
                    if (limit > 2)
                        limit(limit / 2.toLong())
                    else if (limit > 1)
                        limit(limit.toLong())
                }.asResponse { it.toChannel() },
            firebaseProvider.channelsCollection
                .whereGreaterThanOrEqualTo(FIELD_SEARCH_TAG, namePart)

                .apply {
                    if (limit > 2)
                        limit(limit / 2.toLong())
                    else if (limit > 1)
                        limit(limit.toLong())
                }.asResponse { it.toChannel() }
        ).merge()

    override fun createInviteLink(id: String): String {
        return buildString {
            append(URL_BASE)
            append(LINK_CHANNEL)
            append('/')
            append(id)
        }
    }
}

