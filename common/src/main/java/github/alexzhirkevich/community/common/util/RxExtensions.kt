package github.alexzhirkevich.community.common.util




//fun Completable.invoke() : Disposable {
//    return subscribe({},{})
//}

//fun CompletableEmitter.with(completable : Completable) : Disposable =
//    completable.subscribe({onComplete()},{tryOnError(it)})
//
//fun CompletableObserver.with(completable : Completable) : Disposable  =
//        completable.subscribe({onComplete()},{onError(it)})

//inline fun <T : Any> SingleEmitter<List<T>>.parseList(
//        task : Task<QuerySnapshot>,
//        clazz: Class<T>,
//        crossinline onParse : (QuerySnapshot) -> List<T> = { it.map { c -> c.toObject(clazz) }}) =
//    task.addOnSuccessListener {
//        try {
//            onSuccess(onParse(it))
//        } catch (t: Throwable) {
//            tryOnError(t)
//        }
//    }.addOnFailureListener { t ->
//        tryOnError(t)
//    }

//fun <T : Any> SingleEmitter<List<T>>.parseListNonNull(
//        task : Task<QuerySnapshot>,
//        clazz: Class<T>,
//        onParse : (QuerySnapshot) -> List<T> = { it.mapNotNull { c -> c.toObject(clazz) }}) =
//        parseList(task,clazz,onParse)

//data class Mix2<T1, T2>(val v1 : T1, val v2:T2)
//data class Mix3<T1, T2, T3>(val v1 : T1, val v2:T2, val v3:T3)
//data class Mix4<T1, T2, T3, T4>(val v1 : T1, val v2:T2, val v3:T3, val v4:T4)
//data class Mix5<T1, T2, T3, T4, T5>(val v1 : T1, val v2:T2, val v3:T3, val v4:T4, val v5:T5)
//
//fun <T1,T2> mixObservable(obs1 : Observable<T1>, obs2: Observable<T2>) : Observable<Mix2<T1,T2>> {
//
//    return Observable.create {
//        listOf(obs1,obs2).merge(true).map {
//            it.toList().let { col -> Mix2(col[0],col[1]) }
//        }
//    }
//}
//
//fun <T> Collection<Observable<out T>>.merge(waitForAll : Boolean = false) : Observable<Collection<T>> {
//    val disposables = ArrayList<Disposable>(size)
//    val items = ConcurrentHashMap<Int, T>()
//
//    return Observable.create<Collection<T>> { mainObservable ->
//        forEachIndexed { idx, obs ->
//            disposables.add(obs.subscribe(
//                { list ->
//                    synchronized(items) {
//                        items[idx] = list
//                    }
//                    if (!waitForAll || items.size == size)
//                        mainObservable.onNext(items.values)
//                },
//                {
//                    mainObservable.tryOnError(it)
//                },
//                {
//                    mainObservable.onComplete()
//                }
//            ))
//        }
//    }.doFinally {
//        disposables.forEach { it.dispose() }
//    }
//}

//fun <T> ObservableEmitter<T>.with(observable: Observable<T>): Disposable =
//            observable.subscribe(
//                    { t -> onNext(t) },
//                    { ex -> tryOnError(ex) },
//                    { onComplete() }
//            )
