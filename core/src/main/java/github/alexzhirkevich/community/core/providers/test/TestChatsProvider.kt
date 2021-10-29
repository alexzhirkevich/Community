//package github.alexzhirkevich.community.core.providers.test
//
//import github.alexzhirkevich.community.core.entities.imp.Group
//import github.alexzhirkevich.community.core.entities.interfaces.chat.IChat
//import github.alexzhirkevich.community.core.entities.interfaces.IUser
//import github.alexzhirkevich.community.core.providers.interfaces.ChatsProvider
//import io.reactivex.Completable
//import io.reactivex.Maybe
//import io.reactivex.Observable
//
//class TestChatsProvider : ChatsProvider {
//
//    fun newInstance(id :String) =
//            Group(id = id,name = "Test Test",lastMessageId = "test",lastMessageTime = 0)
//
//    override fun getUsers(chatId: String, limit: Int): Observable<Collection<IUser>> = Observable.create {
//    }
//
//    override fun invite(chatId: String, userId: String): Maybe<out IChat> {
//        TODO("Not yet implemented")
//    }
//
////    override fun invite(chatId: String): Maybe<IChat> = Maybe.just(
////            newInstance(chatId)
////    )
//
//    override fun get(id: String): Observable<IChat> = Observable.just(
//            newInstance(id)
//    )
//
//    @ExperimentalStdlibApi
//    override fun getAll(collection: IUser, limit: Int): Observable<Collection<IChat>> {
//        val list = buildList<IChat> {
//            repeat(limit) {
//                add(newInstance("test$it"))
//            }
//        }
//        return Observable.just(list)
//    }
//
//    override fun create(entity: IChat): Completable = Completable.complete()
//
//    override fun delete(entity: IChat): Completable = Completable.complete()
//
//    override fun remove(id: String, collection: IUser): Completable = Completable.complete()
//}