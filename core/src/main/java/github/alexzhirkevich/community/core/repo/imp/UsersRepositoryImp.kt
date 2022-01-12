package github.alexzhirkevich.community.core.repo.imp

import github.alexzhirkevich.community.core.entities.imp.UserImpl
import github.alexzhirkevich.community.core.entities.interfaces.User
import github.alexzhirkevich.community.core.repo.interfaces.FirebaseRepository.Companion.FIELD_NOTIFY_TOKEN
import github.alexzhirkevich.community.core.repo.interfaces.FirebaseRepository.Companion.FIELD_PHONE
import github.alexzhirkevich.community.core.repo.interfaces.FirebaseRepository.Companion.FIELD_USERNAME
import github.alexzhirkevich.community.core.repo.interfaces.FirebaseRepository.Companion.FIELD_USERNAME_SEARCH
import github.alexzhirkevich.community.core.repo.interfaces.UsersRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import github.alexzhirkevich.community.core.*
import github.alexzhirkevich.community.core.asResponse
import github.alexzhirkevich.community.core.repo.interfaces.FirebaseRepository.Companion.COLLECTION_CHANNELS
import github.alexzhirkevich.community.core.repo.interfaces.FirebaseRepository.Companion.COLLECTION_CHATS
import github.alexzhirkevich.community.core.repo.interfaces.FirebaseRepository.Companion.COLLECTION_USERS
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Singleton
class UsersRepositoryImp @Inject constructor() : UsersRepository {

    override val currentUserId: String
        get() = FirebaseAuth.getInstance().currentUser!!.uid

    private val usersCollection = FirebaseFirestore.getInstance()
        .collection(COLLECTION_USERS)

    override fun getNotificationToken(userId: String): Flow<Response<String>> =
            usersCollection.document(userId)
                .asResponse{ it[FIELD_NOTIFY_TOKEN].toString() }

    override fun get(id: String): Flow<Response<User>> =
            usersCollection.document(id).asResponse<UserImpl>()

    override suspend fun create(entity: User) {
        if (entity.id.isNotEmpty()) {
            usersCollection.document(entity.id)
                .set(entity, SetOptions.merge()).await()
        } else {
            val doc =  usersCollection.document()
            entity.id = doc.id
            doc.set(entity).await()
        }
    }

    override suspend fun delete(entity: User) {
        usersCollection
            .document(entity.id).delete()
            .await()
    }

//    override fun findByPhone(vararg phones: String): Observable<List<IUser>> =
//            Observable.fromArray(phones.toList().chunked(10).map { list ->
//                usersCollection.whereIn(PHONE, list).toObservable(User::class.java)
//            })
//                    .flatMapIterable { it.apply{ forEach{ it.observeOn(Schedulers.io()) } }}
//                    .flatMap { it }


    override fun findByPhone(vararg phones: String): Flow<Collection<Response<User>>> =
            phones.toList().chunked(10).map { list ->
                usersCollection.whereIn(FIELD_PHONE, list)
                    .asResponse<UserImpl>()
            }.combine().map(Iterable<Iterable<Response<User>>>::flatten)

    override fun findByTag(username: String): Flow<Response<User>> =
            usersCollection.whereEqualTo(FIELD_USERNAME, username)
                .asResponse<UserImpl>().map(Iterable<Response<User>>::first)

    override fun findByUsernameNearly(username: String, limit: Int) : Flow<Collection<Response<User>>> {
        var querry = usersCollection
            .whereGreaterThanOrEqualTo(FIELD_USERNAME_SEARCH,username)
        if (limit > -1){
            querry = querry.limit(limit.toLong())
        }
        return querry.asResponse<UserImpl>()
    }

    override fun getChatsIds(userId: String, limit: Int): Flow<Collection<Response<String>>> =
        usersCollection.document(userId)
            .collection(COLLECTION_CHATS)
            .limit(limit.toLong())
            .asResponse(DocumentSnapshot::getId)


    override fun getChannelIds(userId: String, limit: Int): Flow<Collection<Response<String>>> =
        usersCollection.document(userId)
            .collection(COLLECTION_CHANNELS)
            .limit(limit.toLong())
            .asResponse(DocumentSnapshot::getId)

//    override fun isUsernameAvailable(username: String): Observable<Boolean> =
////            findByUsername(username).map { it.id == currentUserId }
//            firebaseProvider.usersCollection.whereEqualTo(FIELD_USERNAME, username).toObservable {
//                it[FIELD_ID].toString()
//            }.map { it.isEmpty() }

}

