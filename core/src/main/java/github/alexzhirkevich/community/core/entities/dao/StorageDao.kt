//package github.alexzhirkevich.community.core.entities.dao
//
//import android.graphics.Bitmap
//import io.reactivex.Completable
//import io.reactivex.Maybe
//
//interface StorageDao {
//
//    fun saveImage(img: Bitmap,name:String): Completable
//
//    fun loadImage(uri: String): Maybe<Bitmap>
//
//    fun saveVoice(bytes: ByteArray, name: String): Completable
//
//    fun loadVoice(uri: String): Maybe<ByteArray>
//}