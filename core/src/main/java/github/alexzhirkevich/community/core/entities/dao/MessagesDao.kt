//package github.alexzhirkevich.community.core.entities.dao
//
//import androidx.room.*
//import github.alexzhirkevich.community.core.entities.imp.Group
//import github.alexzhirkevich.community.core.entities.imp.MediaMessage
//import github.alexzhirkevich.community.core.entities.imp.Message
//import github.alexzhirkevich.community.core.entities.imp.VoiceMessage
//import io.reactivex.Completable
//import io.reactivex.Maybe
//import io.reactivex.Single
//
//@Dao
//interface MessagesDao  {
//
//    @Transaction
//    fun add(entity: Message) {
//        when (entity) {
//            is VoiceMessage -> addVoice(entity)
//            is MediaMessage -> addMedia(entity)
//            else -> addDefault(entity)
//        }
//    }
//
//    @Query("SELECT EXISTS(SELECT id from ${Message.TABLE_NAME} WHERE id = :id)")
//    fun contains(id: String): Single<Boolean>
//
//    @Query("DELETE FROM ${Message.TABLE_NAME} WHERE id is :id")
//    fun delete(id: String) : Completable
//
//    @Transaction
//    fun delete(message : Message) {
//        when (message) {
//            is VoiceMessage -> deleteVoice(message.id)
//            is MediaMessage -> deleteMedia(message.id)
//            else -> delete(message.id)
//        }
//    }
//
//    @Query("SELECT * FROM ${Message.TABLE_NAME} WHERE id = :id")
//    fun get(id: String): Maybe<Message>
//
////    @Transaction
////    fun getAll(chatId : String) : Single<List<Message>> = Single.create {
////        val msgs = mutableListOf<Message>()
////        getAllDefault(chatId).doOnSuccess { list ->
////            msgs.addAll(list)
////            getAllVoice(chatId).doOnSuccess { list2 ->
////                msgs.addAll(list2)
////                getAllMedia(chatId).doOnSuccess { list3 ->
////                    msgs.addAll(list3)
////                    msgs.sortBy { msg -> msg.time }
////                    it.onSuccess(msgs)
////                }.subscribe()
////            }.subscribe()
////        }.subscribe()
////    }
//
//    @Query("SELECT last_message_id FROM ${Group.TABLE_NAME} WHERE id = :chatId LIMIT 1")
//    fun lastMessageId(chatId: String): Single<String>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun addDefault(message : Message) : Completable
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun addVoice(message: VoiceMessage) : Completable
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun addMedia(message: MediaMessage) : Completable
//
//    @Query("DELETE FROM ${VoiceMessage.TABLE_NAME} WHERE id = :id")
//    fun deleteVoice(id : String) : Completable
//
//    @Query("DELETE FROM ${MediaMessage.TABLE_NAME} WHERE id = :id")
//    fun deleteMedia(id : String) : Completable
//
//    @Query("SELECT * FROM ${Message.TABLE_NAME} WHERE chat_id = :chatId")
//    fun getAllDefault(chatId: String) : Single<List<Message>>
//
//    @Query("SELECT * FROM ${VoiceMessage.TABLE_NAME} WHERE chat_id = :chatId")
//    fun getAllVoice(chatId: String) : Single<List<VoiceMessage>>
//
//    @Query("SELECT * FROM ${MediaMessage.TABLE_NAME} WHERE chat_id = :chatId")
//    fun getAllMedia(chatId: String) : Single<List<MediaMessage>>
//}