//package github.alexzhirkevich.community.core.entities.dao
//
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import com.community.messenger.app.data.ChatApplication.Companion.AppContext
//import io.reactivex.Completable
//import io.reactivex.Maybe
//import java.io.File
//import java.io.FileInputStream
//import java.io.FileOutputStream
//
//class AppStorageDao : StorageDao {
//
//    override fun saveImage(img: Bitmap, name: String): Completable = Completable.create {
//        //saveFile(uri,"${AppContext.filesDir.path}${File.pathSeparator}$IMAGES${File.pathSeparator}$uri"
//        try {
//            val file = File(name).apply { createNewFile() }
//            FileOutputStream(file).use { fos ->
//                if (img.compress(Bitmap.CompressFormat.JPEG, 100, fos)){
//                    it.onComplete()
//                } else{
//                    it.tryOnError(Exception("Bitmap compress failure"))
//                }
//            }
//        } catch (t: Throwable) {
//            it.tryOnError(t)
//        }
//    }
//
//    override fun loadImage(uri: String): Maybe<Bitmap> = Maybe.create {
//        try {
//            val file = File(AppContext.filesDir.path, "$IMAGES${File.pathSeparator}$uri")
//            if (file.exists()) {
//                val data = file.readBytes()
//                it.onSuccess(BitmapFactory.decodeFile(file.path))
//            } else {
//                it.onComplete()
//            }
//        }catch (t : Throwable){
//            it.tryOnError(t)
//        }
//    }
//
//    override fun saveVoice(bytes: ByteArray,name : String): Completable  = Completable.create {
//        try {
//            val file = File(name).apply { createNewFile() }
//            FileOutputStream(file).use { fos ->
//                fos.write(bytes)
//            }
//        } catch (t: Throwable) {
//            it.tryOnError(t)
//        }
//    }
//
//    override fun loadVoice(uri: String): Maybe<ByteArray> = Maybe.create {
//        try {
//            val file = File(AppContext.filesDir.path, "$VOICES${File.pathSeparator}$uri")
//            if (file.exists()) {
//                it.onSuccess(FileInputStream(file).readBytes())
//            } else {
//                it.onComplete()
//            }
//        } catch (t : Throwable) {
//            it.tryOnError(t)
//        }
//    }
//
////    private fun saveFile(uri : String, file : String) = Completable.create {
////        val url = URL(uri)
////        val c: HttpURLConnection = url.openConnection() as HttpURLConnection
////        c.requestMethod = "GET"
////        c.connect()
////
////        File(file).apply {
////            if (!exists())
////                createNewFile()
////        }
////
////        FileOutputStream(file).use { fos ->
////
////            val buffer = ByteArray(1024)
////
////            var len: Int
////
////            while (c.inputStream.read(buffer).also { len = it } != -1) {
////                fos.write(buffer, 0, len) //Write new file
////            }
////        }
////    }
//
//    companion object{
//        private const val IMAGES = "images"
//        private const val VOICES = "voices"
//    }
//}