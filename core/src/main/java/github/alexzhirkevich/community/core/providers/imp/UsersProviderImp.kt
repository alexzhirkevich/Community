package github.alexzhirkevich.community.core.providers.imp

import github.alexzhirkevich.community.core.entities.imp.UserImpl
import github.alexzhirkevich.community.core.entities.interfaces.User
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider.Companion.FIELD_NOTIFY_TOKEN
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider.Companion.FIELD_PHONE
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider.Companion.FIELD_USERNAME
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider.Companion.FIELD_USERNAME_SEARCH
import github.alexzhirkevich.community.core.providers.interfaces.UsersProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import github.alexzhirkevich.community.core.*
import github.alexzhirkevich.community.core.asResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Singleton
class UsersProviderImp @Inject constructor(
        private val firebaseProvider: FirebaseProvider
) : UsersProvider {

    override val currentUserId: String
        get() = FirebaseAuth.getInstance().currentUser!!.uid

    override fun getNotificationToken(userId: String): Flow<Response<String>> =
            firebaseProvider.usersCollection.document(userId)
                .asResponse{ it[FIELD_NOTIFY_TOKEN].toString() }

    override fun get(id: String): Flow<Response<User>> =
            firebaseProvider.usersCollection.document(id).asResponse<UserImpl>()

    override suspend fun create(entity: User) {
        if (entity.id.isNotEmpty()) {
            firebaseProvider.usersCollection.document(entity.id)
                .set(entity, SetOptions.merge()).await()
        } else {
            val doc =  firebaseProvider.usersCollection.document()
            entity.id = doc.id
            doc.set(entity).await()
        }
    }

    override suspend fun delete(entity: User) {
        firebaseProvider.usersCollection
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
                firebaseProvider.usersCollection.whereIn(FIELD_PHONE, list)
                    .asResponse<UserImpl>()
            }.combine().map { it.flatten() }

    override fun findByUsername(username: String): Flow<Response<User>> =
            firebaseProvider.usersCollection.whereEqualTo(FIELD_USERNAME, username)
                .asResponse<UserImpl>().map { it.first() }

    override fun findByUsernameNearly(username: String, limit: Int) : Flow<Collection<Response<User>>> {
        var querry = firebaseProvider.usersCollection
            .whereGreaterThanOrEqualTo(FIELD_USERNAME_SEARCH,username)
        if (limit > -1){
            querry = querry.limit(limit.toLong())
        }
        return querry.asResponse<UserImpl>().map { it }
    }

//    override fun isUsernameAvailable(username: String): Observable<Boolean> =
////            findByUsername(username).map { it.id == currentUserId }
//            firebaseProvider.usersCollection.whereEqualTo(FIELD_USERNAME, username).toObservable {
//                it[FIELD_ID].toString()
//            }.map { it.isEmpty() }

}

