package github.alexzhirkevich.community.core

import android.net.Uri
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider.Companion.FIELD_LAST_MESSAGE
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider.Companion.FIELD_REPLY_TO
import github.alexzhirkevich.community.core.providers.interfaces.FirebaseProvider.Companion.FIELD_TYPE
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.UploadTask
import github.alexzhirkevich.community.core.entities.imp.*
import github.alexzhirkevich.community.core.entities.interfaces.Channel
import github.alexzhirkevich.community.core.entities.interfaces.Chat
import github.alexzhirkevich.community.core.entities.interfaces.Message
import github.alexzhirkevich.community.core.entities.interfaces.Sendable
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

internal fun Map<String,*>.toChannel() : Channel =
    ChannelImpl().apply {
        fillObject(this)
        lastMessage = (getOrElse(FIELD_LAST_MESSAGE, { null }) as? Map<String, *>)?.toPost()
    }

internal fun Map<String,*>.toChat() : Chat? {

    return when (getOrElse(FIELD_TYPE) {null}) {
        Chat.TYPE_DIALOG -> toObject<DialogImpl>()
        Chat.TYPE_GROUP -> toObject<GroupImpl>()
        else -> null
    }?.apply {
        lastMessage = (getOrElse(FIELD_LAST_MESSAGE) { null } as? Map<String, *>)?.toMessage()
    }
}

internal fun Map<String,*>.toMessage() : Sendable? {
    val type = getOrElse(FIELD_TYPE, { null })
    return if (type != null && type is Number) {
        when (type) {
            Sendable.TYPE_SYSTEM ->
                toObject<SystemMessageImpl>()
            else ->
                toObject<MessageImpl>().apply {
                    if (FIELD_REPLY_TO in keys) {
                        replyTo = (get(FIELD_REPLY_TO) as? Map<String, *>)?.toMessage() as? Message
                    }
                }
        }
    } else null
}

internal fun Map<String,*>.toPost() : Sendable? {
    val type = getOrElse(FIELD_TYPE, { null })
    return if (type != null && type is Number) {
        when (type) {
            Sendable.TYPE_SYSTEM -> toObject<SystemMessageImpl>()
            else -> toObject<PostImpl>()
        }
    } else null
}

@ExperimentalCoroutinesApi
internal fun FileDownloadTask.asLoadingFlow() : Flow<Loading<Unit>> = callbackFlow {
    val tasks = listOf(
        addOnSuccessListener {
            trySend(Loading.Success(Unit))
        },
        addOnFailureListener{
            throw it
        },
        addOnProgressListener {
            trySend(Loading.Progress(
                it.bytesTransferred.toFloat()/it.totalByteCount))
        }
    )
    awaitClose{
        tasks.onEach { close() }
    }
}.catch { err -> emit(Loading.Error(err)) }

@ExperimentalCoroutinesApi
internal fun UploadTask.asLoadingFlow() : Flow<Loading<Uri>> = callbackFlow<Loading<Uri>> {
    val tasks = listOf(
        addOnSuccessListener {
            launch(Dispatchers.IO) {
                it.metadata?.reference?.downloadUrl?.await()?.let {
                    trySend(Loading.Success(it))
                }
            }
        },
        addOnFailureListener {
            throw it
        },
        addOnProgressListener {
            trySend(Loading.Progress(it.bytesTransferred.toFloat() / it.totalByteCount))
        }
    )

    awaitClose {
        tasks.onEach { close() }
    }
}.catch { err -> emit(Loading.Error(err)) }

suspend fun CollectionReference.delete(batchSize : Int = 10) {
    val query = if (batchSize > 0) this else limit(batchSize.toLong())

    var deletedCount: Int
    do {
        val qs = query.get().await()
        deletedCount = qs.size()
        qs.map { it.reference.delete() }.awaitAll()
    } while (deletedCount == batchSize)
}

@ExperimentalCoroutinesApi
internal fun <T> Query.asFlow(ignoreError : Boolean = true, parser: (DocumentSnapshot) -> T?) : Flow<Collection<T>> {
    return callbackFlow<Collection<T>> {
        val reg = addSnapshotListener { qs, error ->
            if (error != null && !ignoreError) {
                throw error
            } else {
                if (qs != null) {
                    trySend(qs.mapNotNull(parser))
                } else
                    trySend(emptyList<T>())
            }
        }
        awaitClose { reg.remove() }
    }
}

@ExperimentalCoroutinesApi
internal inline fun <reified T> Query.asResponse() : Flow<Collection<Response<T>>> {
    return callbackFlow<Collection<Response<T>>> {
        val reg = addSnapshotListener { qs, error ->
            if (error != null) {
                trySend(listOf(Response.Error(error)))
            } else if (qs != null) {
                trySend(qs.asResponses())
            }
        }
        awaitClose { reg.remove() }
    }
}

suspend fun <T> Collection<Task<T>>.awaitAll() : Collection<T> =
    map { it.await() }


suspend fun <T> Task<T>.await() : T = suspendCoroutine<T> { cont ->
    val reg = addOnCompleteListener { t ->
         t.result?.let { cont.resume(it) } ?:
         t.exception?.let { cont.resumeWithException(it) }
    }
}

fun <K,T> Map<K, Flow<T>>.combine() : Flow<Map<K,T>> =

    when (size) {
        0 -> flowOf(emptyMap())
        1 -> values.first().map { mapOf(keys.first() to it) }
        else -> {

            var a = get(keys.first())!!.map { mapOf(keys.first() to it) }
            for (i in 1 until size) {
                val keys = keys.toList()
                a = a.combineTransform(
                    get(keys[i])!!.map { mapOf(keys.first() to it) },
                ) { x, y -> x + y }
            }
            a
        }
    }

inline fun <reified T> Collection<Flow<T>>.combine() : Flow<Collection<T>> =
    combine(*this.toTypedArray()){ it.toList() }

@ExperimentalCoroutinesApi
internal fun <T> Query.asResponse(parser : (Map<String,*>) -> T)
    : Flow<Collection<Response<T>>> {
    return callbackFlow<Collection<Response<T>>> {
        val reg = addSnapshotListener { qs, error ->
            if (error != null) {
               trySend(listOf(Response.Error(error)))
            } else if (qs != null) {
                trySend(qs.asResponses(parser))
            }
        }
        awaitClose{
            reg.remove()
        }
    }.catch { err -> emit(listOf(Response.Error(err))) }
}

internal fun <T> DocumentReference.asFlow(parser : (Map<String,*>) -> T){

}

@ExperimentalCoroutinesApi
internal fun <T> DocumentReference.asResponse(parser : (Map<String,*>) -> T) : Flow<Response<T>>{

    return callbackFlow<Response<T>> {
        val reg = addSnapshotListener { ds, error ->
            if (error != null) {
               trySend(Response.Error(error))
            } else {
                if (ds != null)
                    trySend(ds.asResponse(parser))
            }
        }
        awaitClose { reg.remove() }
    }
}
@ExperimentalCoroutinesApi
internal inline fun<reified T> DocumentReference.asResponse() : Flow<Response<T>> =
   asResponse { it.toObject() }


//internal inline fun<reified T> Task<DocumentSnapshot>.asResponse() : Single<Response<T>> {
//    var reg: ListenerRegistration? = null
//
//    return Single.create<Response<T>> {
//        addOnSuccessListener { it.asResponse<T>() }
//    }.doFinally { reg?.remove() }
//}

internal fun <T> QuerySnapshot.asResponses(parser: (Map<String, *>) -> T)
        : Collection<Response<T>> = documents.mapNotNull {
    try {
        it.asResponse(parser)
    }catch (t :Throwable){
        null
    }
}


internal inline fun <reified T> QuerySnapshot.asResponses() : Collection<Response<T>> =
    asResponses { it.toObject() }

internal fun  <T> DocumentSnapshot.asResponse(parser: (Map<String, *>) -> T): Response<T> =
        Response.Success(
                value = parser(data!!),
                isFromCache = metadata.isFromCache,
                hasPendingWrites = !metadata.hasPendingWrites()
            )



internal inline fun <reified T> DocumentSnapshot.asResponse(): Response<T> =
    Response.Success(
            value = toObject(T::class.java)!!,
            isFromCache = metadata.isFromCache,
            hasPendingWrites = !metadata.hasPendingWrites()
        )
