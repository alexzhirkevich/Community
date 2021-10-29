//package github.alexzhirkevich.community.core.entities.dao
//
//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//import github.alexzhirkevich.community.core.entities.imp.Post
//import io.reactivex.Completable
//import io.reactivex.Maybe
//import io.reactivex.Single
//
//@Dao
//interface PostsDao : EntityDao<Post> {
//
//    @Query("SELECT * FROM ${Post.TABLE_NAME} WHERE id = :id LIMIT 1")
//    override fun get(id: String): Maybe<Post>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    override fun add(entity: Post) : Completable
//
//    @Query("SELECT EXISTS(SELECT id from ${Post.TABLE_NAME} WHERE id = :id)")
//    override fun contains(id: String): Single<Boolean>
//
//    @Query("DELETE FROM ${Post.TABLE_NAME} WHERE id = :id")
//    override fun delete(id: String) : Completable
//
//    @Query("SELECT * FROM ${Post.TABLE_NAME} WHERE channel_id = :channelId ORDER BY time LIMIT CASE WHEN (:limit > -1) THEN :limit END")
//    fun getAll(channelId : String, limit: Int) : Single<List<Post>>
//
//    @Query("SELECT * FROM ${Post.TABLE_NAME} WHERE channel_id = :channelId ORDER BY time LIMIT 1")
//    fun lastPost(channelId: String): Single<Post>
//}