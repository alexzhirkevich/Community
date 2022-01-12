package github.alexzhirkevich.community.core.repo.imp.storage

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import android.util.Size
import android.webkit.MimeTypeMap
import androidx.core.net.toFile
import androidx.core.net.toUri
import github.alexzhirkevich.community.core.BuildConfig
import github.alexzhirkevich.community.core.Util
import java.io.File
import java.io.FileOutputStream
import kotlin.math.min
import kotlin.math.roundToInt

internal object UploadUtil{

    fun getFileExtension(context: Context, uri: Uri): String {
        return kotlin.runCatching {
            if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
                val mime = MimeTypeMap.getSingleton()
                mime.getExtensionFromMimeType(context.contentResolver.getType(uri))
            } else {
                uri.path?.let {
                    MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(File(it)).toString())
                }
            }
        }.getOrNull().orEmpty()
    }

    fun removeFromDownloads(context: Context, dirName: String, fileName: String){
        kotlin.runCatching {
            val target = File(context.filesDir, "$dirName/${fileName}")
            target.delete()
        }
    }

    fun copyToDownloads(context: Context, uri: Uri, dirName : String, fileName : String) {
        runCatching {
            val target = File(context.filesDir, "$dirName/${fileName}")
            uri.toFile().copyTo(target)
        }
    }

    fun createImageThumbnail(context: Context, uri: Uri) : Uri? {
        val thumbnail = runCatching {
            val file = uri.toFile()
            val bitmapOptions = BitmapFactory.Options()
            bitmapOptions.inJustDecodeBounds = true
            BitmapFactory.decodeFile(file.absolutePath, bitmapOptions)

            val desiredSize = 100

            if (bitmapOptions.outWidth <= desiredSize &&
                bitmapOptions.outHeight <= desiredSize)
                return uri

            val widthScale = bitmapOptions.outWidth.toFloat() / 100
            val heightScale = bitmapOptions.outHeight.toFloat() / 100
            val scale = min(widthScale, heightScale)

            var sampleSize = 1
            while (sampleSize < scale) {
                sampleSize *= 2
            }
            bitmapOptions.inSampleSize = sampleSize
            bitmapOptions.inJustDecodeBounds = false

            BitmapFactory.decodeFile(file.absolutePath, bitmapOptions)

        }.onFailure {
            if (BuildConfig.DEBUG) {
                Log.e(javaClass.simpleName,
                    "Failed to create image thumbnail", it)
            }
        }.getOrNull() ?: return null

        return tmpSave(context, thumbnail).also { thumbnail.recycle() }
    }

    fun compressBitmap(bitmap: Bitmap, desiredSize : Int = 100) : Bitmap {
        if (bitmap.width <= desiredSize &&
            bitmap.height <= desiredSize)
            return bitmap

        val widthScale = bitmap.width.toFloat() / desiredSize
        val heightScale = bitmap.height.toFloat() / desiredSize
        val scale = min(widthScale, heightScale)


        val scaled = Bitmap.createScaledBitmap(
            bitmap,
            (bitmap.width * scale).roundToInt(),
            (bitmap.height * scale).roundToInt(),
            false);

        return scaled
    }

    fun createVideoThumbnail(context: Context, uri: Uri) : Uri? {
        val mediaMetadataRetriever = MediaMetadataRetriever();
        val bm = try {
            mediaMetadataRetriever.setDataSource(context,uri);
            mediaMetadataRetriever.frameAtTime?.let {
                compressBitmap(it)
            }
        } catch (t : Throwable) {
            if (BuildConfig.DEBUG)
                Log.e(javaClass.simpleName,
                    "Failed to create video thumbnail", t)
            return null
        } finally {
            mediaMetadataRetriever.release()
        } ?: return null

        return tmpSave(context,bm).also { bm.recycle() }
    }

    fun getVideoSize(context: Context, uri: Uri) : Size? {

        val metaRetriever = MediaMetadataRetriever()
        return try {
            metaRetriever.setDataSource(context, uri)

            val height = metaRetriever.extractMetadata(
                MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT
            )?.toInt() ?: return null

            val width = metaRetriever.extractMetadata(
                MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH
            )?.toInt() ?: return null

            Size(width, height)
        } catch (t: Throwable) {
            if (BuildConfig.DEBUG)
                Log.e(
                    javaClass.simpleName,
                    "Failed to create video thumbnail", t
                )
            null
        } finally {
            metaRetriever.release()
        }
    }

    fun getImageSize(uri: Uri) : Size? {
        return kotlin.runCatching {

            val file = uri.toFile()
            val bitmapOptions = BitmapFactory.Options()
            bitmapOptions.inJustDecodeBounds = true
            BitmapFactory.decodeFile(file.absolutePath, bitmapOptions)

            return Size(bitmapOptions.outWidth,bitmapOptions.outHeight)
        }.onFailure{
            if (BuildConfig.DEBUG)
                Log.e(javaClass.simpleName,
                    "Failed to get image size", it)
        }.getOrNull()
    }

    fun getAudioLength(context: Context, uri: Uri) : Long?{
        val metaRetriever = MediaMetadataRetriever()
        return try {
            metaRetriever.setDataSource(context, uri)

            return metaRetriever.extractMetadata(
                MediaMetadataRetriever.METADATA_KEY_DURATION
            )?.toLong()

        } catch (t: Throwable) {
            if (BuildConfig.DEBUG)
                Log.e(
                    javaClass.simpleName,
                    "Failed to create video thumbnail", t
                )
            null
        } finally {
            metaRetriever.release()
        }
    }

    fun tmpSave(context: Context, bitmap: Bitmap): Uri? {
        return try {
            val file = File(
                context.cacheDir,
                System.currentTimeMillis().toString() + Util.autoId()
            )
            FileOutputStream(file).use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it)
                it.flush()
            }
            file.toUri()
        } catch (t : Throwable){
            if (BuildConfig.DEBUG)
                Log.e(javaClass.simpleName, "Failed to tmpSave bitmap", t)
            null
        }
    }
}
