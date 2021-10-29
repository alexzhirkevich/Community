//package github.alexzhirkevich.community.core.providers.test
//
//import github.alexzhirkevich.community.core.entities.imp.Event
//import github.alexzhirkevich.community.core.entities.interfaces.IEvent
//import github.alexzhirkevich.community.core.providers.interfaces.EventsProvider
//import com.google.firebase.auth.FirebaseAuth
//import io.reactivex.Completable
//import io.reactivex.Observable
//import kotlin.random.Random
//
//class TestEventsProvider : EventsProvider {
//
//    private val curUID = FirebaseAuth.getInstance().uid!!
//    private var counter = 0
//
//    override fun get(id: String): Observable<IEvent> {
//        TODO("Not yet implemented")
//    }
//
//    override fun create(entity: IEvent): Completable = Completable.complete()
//
//    override fun delete(entity: IEvent): Completable = Completable.complete()
//
//    override fun getAll(limit: Int): Observable<Collection<IEvent>> {
//        return Observable.just(collectionOf(limit, curUID)
//                .apply { addAll(collectionOf(limit, "test")) })
//    }
//
//    private fun collectionOf(count: Int, creator: String): MutableCollection<IEvent> {
//        val col = mutableListOf<IEvent>()
//        repeat(count / 4) {
//            col.add(Event(id = "event${counter++}", name = "Event $it", time = System.currentTimeMillis() + Random.nextLong(10000000, 1000000000),
//                    creatorId = creator, address = "Event address $it",
//                    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod " +
//                            "tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, " +
//                            "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
//                            "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu " +
//                            "fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in " +
//                            "culpa qui officia deserunt mollit anim id est laborum",
//                    imageUri = if (it % 2 == 0) "https://1.bp.blogspot.com/-xPyGLEpeq_s/WBNzWjmEtDI/AAAAAAAAObM/" +
//                            "AFMpRL24rdseOJrLFQHXUcnZJpkMnP5mwCLcB/s1600/Wide%2BBackgrounds%2B1.jpg" else ""))
//        }
//        repeat(count / 8) {
//            col.add(Event(id = "event${counter++}", name = "Event $it - ended", time = System.currentTimeMillis() - Random.nextLong(1000000, 1000000000),
//                    creatorId = creator, address = "Event address $it", isEnded = true,
//                    imageUri = if (it % 2 == 0) "https://1.bp.blogspot.com/-xPyGLEpeq_s/WBNzWjmEtDI/AAAAAAAAObM/" +
//                            "AFMpRL24rdseOJrLFQHXUcnZJpkMnP5mwCLcB/s1600/Wide%2BBackgrounds%2B1.jpg" else ""))
//            col.add(Event(id = "event${counter++}", name = "Event $it - cancelled", time = System.currentTimeMillis() + Random.nextLong(1000000, 1000000000),
//                    creatorId = creator, address = "Event address $it", isValid = false,
//                    imageUri = if (it % 2 == 0) "https://1.bp.blogspot.com/-xPyGLEpeq_s/WBNzWjmEtDI/AAAAAAAAObM/" +
//                            "AFMpRL24rdseOJrLFQHXUcnZJpkMnP5mwCLcB/s1600/Wide%2BBackgrounds%2B1.jpg" else ""))
//        }
//        return col
//    }
//
//    override fun remove(id: String): Completable = Completable.complete()
//}