//package github.alexzhirkevich.community.core.entities.dao
//
//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//import github.alexzhirkevich.community.core.entities.imp.Group
//import io.reactivex.Completable
//import io.reactivex.Maybe
//import io.reactivex.Single
//
//@Dao
//interface ChatsDao : EntityDao<Group> {
//
//    @Query("SELECT * FROM ${Group.TABLE_NAME} WHERE id = :id LIMIT 1")
//    override fun get(id: String): Maybe<Group>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    override fun add(entity: Group): Completable
//
//    @Query("DELETE FROM ${Group.TABLE_NAME} WHERE id = :id")
//    override fun delete(id: String): Completable
//
//    @Query("SELECT EXISTS(SELECT id from ${Group.TABLE_NAME} WHERE id = :id)")
//    override fun contains(id: String): Single<Boolean>
//
//    @Query("SELECT * FROM ${Group.TABLE_NAME} ORDER BY last_message_time LIMIT CASE WHEN (:limit>-1) THEN :limit END")
//    fun getAll(limit:Int): Single<List<Group>>
//}