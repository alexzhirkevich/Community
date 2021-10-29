//package github.alexzhirkevich.community.core.providers.test
//
//import github.alexzhirkevich.community.core.entities.imp.Message
//import github.alexzhirkevich.community.core.entities.interfaces.chat.IChat
//import github.alexzhirkevich.community.core.entities.interfaces.message.IMessagable
//import github.alexzhirkevich.community.core.providers.interfaces.MessagesProvider
//import io.reactivex.Completable
//import io.reactivex.Observable
//
//class TestMessagesProvider : MessagesProvider {
//
//    fun newInstance(id : String) =
//            Message(id = id,chatId = "test",text = "Test message text",senderId = "test",
//            time = System.currentTimeMillis())
//
//
//    override fun last(chatId: String): Observable<IMessagable> = Observable.just(
//            newInstance("test")
//    )
//
//    override fun get(id: String, collectionID: String): Observable<IMessagable> = Observable.just(
//            newInstance(id)
//    )
//    override fun getAll(collection: IChat, limit: Int): Observable<Collection<IMessagable>> =
//            Observable.just(
//                    listOf(
//                            newInstance("test1"),
//                            newInstance("test2"),
//                            newInstance("test3")
//                    )
//            )
//
//    override fun create(entity: IMessagable): Completable = Completable.complete()
//
//    override fun delete(entity: IMessagable): Completable = Completable.complete()
//
//    override fun remove(id: String, collection: IChat): Completable = Completable.complete()
//}