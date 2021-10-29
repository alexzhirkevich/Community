//package github.alexzhirkevich.community.core.entities.dao
//
//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//import github.alexzhirkevich.community.core.entities.imp.User
//import io.reactivex.Completable
//import io.reactivex.Maybe
//import io.reactivex.Single
//
//@Dao
//interface UsersDao : EntityDao<User> {
//
//    @Query("SELECT * FROM ${User.TABLE_NAME} WHERE id = :id LIMIT 1")
//    override fun get(id: String): Maybe<User>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    override fun add(entity: User): Completable
//
//    @Query("DELETE FROM ${User.TABLE_NAME} WHERE id = :id")
//    override fun delete(id: String): Completable
//
//    @Query("SELECT EXISTS(SELECT id from ${User.TABLE_NAME} WHERE id = :id)")
//    override fun contains(id: String): Single<Boolean>
//
//}