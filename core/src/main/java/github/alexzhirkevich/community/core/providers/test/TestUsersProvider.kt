//package github.alexzhirkevich.community.core.providers.test
//
//import github.alexzhirkevich.community.core.entities.imp.User
//import github.alexzhirkevich.community.core.entities.interfaces.IUser
//import github.alexzhirkevich.community.core.providers.interfaces.UsersProvider
//import com.google.firebase.auth.FirebaseAuth
//import io.reactivex.Completable
//import io.reactivex.Observable
//
//class TestUsersProvider :UsersProvider{
//
//    override val currentUserId: String
//        get() =  FirebaseAuth.getInstance().currentUser!!.uid
//    override fun getNotificationToken(userId: String): Observable<String> = Observable.error(NotImplementedError())
//
//    override fun findByPhone(vararg phones: String): Observable<Collection<IUser>> {
//        TODO("Not yet implemented")
//    }
//
//    override fun findByUsername(username: String): Observable<IUser> {
//        TODO("Not yet implemented")
//
//    }
//
//    override fun findByUsernameNearly(username: String, limit: Int): Observable<Collection<IUser>> {
//        TODO("Not yet implemented")
//    }
//
////    override fun isUsernameAvailable(username: String): Observable<Boolean> {
////        TODO("Not yet implemented")
////    }
//
//
//    override fun get(id: String): Observable<IUser> = Observable.just(
//            User(id = "test",imageUri = "",name = "Test",isOnline = true)
//    )
//
//
////    override fun getAll(collection: IUserContainer, limit: Int): Observable<List<IUser>>
////            = Observable.just(
////            listOf(
////                    User(id = "test1",imageUri = "",name = "Test",isOnline = true),
////                    User(id = "test2",imageUri = "",name = "Test",isOnline = true),
////                    User(id = "test3",imageUri = "",name = "Test",isOnline = true)
////            )
////    )
//
//    override fun create(entity:IUser): Completable = Completable.complete()
//
//    override fun delete(entity: IUser): Completable = Completable.complete()
//
//}