//package github.alexzhirkevich.community.core.entities.dao
//
//import com.community.firerecadapter.IEntity
//import io.reactivex.Completable
//import io.reactivex.Maybe
//import io.reactivex.Single
//
//interface EntityDao<Entity : IEntity>  {
//
//    fun get(id : String) : Maybe<Entity>
//
//    fun add(entity: Entity): Completable
//
//    fun delete(id : String): Completable
//
//    fun contains(id : String) : Single<Boolean>
//}