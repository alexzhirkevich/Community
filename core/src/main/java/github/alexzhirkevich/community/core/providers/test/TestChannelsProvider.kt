//package github.alexzhirkevich.community.core.providers.test
//
//import github.alexzhirkevich.community.core.entities.imp.ChannelAdmin
//import github.alexzhirkevich.community.core.entities.interfaces.chat.IChannel
//import github.alexzhirkevich.community.core.entities.interfaces.IUser
//import github.alexzhirkevich.community.core.providers.interfaces.ChannelsProvider
//import io.reactivex.Completable
//import io.reactivex.Observable
//import io.reactivex.Single
//
//class TestChannelsProvider : ChannelsProvider {
//    override fun getUsers(channelId: String, limit: Int): Observable<List<IUser>> = Observable.create {  }
//    override fun find(namePart: String, limit: Int): Observable<Collection<IChannel>> {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun join(channelId: String): Single<IChannel> = get(channelId).singleOrError()
//
//    override fun getAdmins(channelId: String): Observable<List<ChannelAdmin>> =
//            getUsers(channelId).map { list ->
//                list.map { user -> ChannelAdmin(id = user.id) }
//            }
//
//    override fun getSubscribersCount(channelId: String): Observable<Long> {
//        TODO("Not yet implemented")
//    }
//
//    override fun getByTag(tag: String): Observable<IChannel> = get(tag).map {
//        it.tag = tag
//        it
//    }
//
//    override fun findByTag(tag: String): Observable<IChannel> {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun get(id: String): Observable<IChannel> =
//            Observable.just(github.alexzhirkevich.community.core.entities.imp.Channel(id = id))
//
//
//    override fun getAll(collection: IUser, limit: Int): Observable<Collection<IChannel>> {
//        val list = mutableListOf<github.alexzhirkevich.community.core.entities.imp.Channel>()
//        for (i in 1..limit){
//            list.add(
//                github.alexzhirkevich.community.core.entities.imp.Channel(
//                    id = "test$i",
//                    name = "Channel Name",
//                    lastPostId = "test",
//                    lastPostTime = System.currentTimeMillis()
//                )
//            )
//        }
//        return Observable.just(list)
//    }
//
//    override fun create(entity: IChannel): Completable = Completable.complete()
//
//    override fun delete(entity: IChannel): Completable = Completable.complete()
//
//    override fun remove(id: String, collection: IUser): Completable = Completable.complete()
//
//    override fun createInviteLink(id: String): String {
//        TODO("Not yet implemented")
//    }
//}