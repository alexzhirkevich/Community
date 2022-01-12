package github.alexzhirkevich.community.core

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import android.util.Size
import androidx.core.net.toFile
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream
import java.security.SecureRandom
import java.util.*
import kotlin.math.min
import kotlin.math.roundToInt

object Util {
    private const val AUTO_ID_LENGTH = 20

    private const val AUTO_ID_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"

    private val rand: Random = SecureRandom()

    fun autoId(size : Int = AUTO_ID_LENGTH): String  = buildString {
        val maxRandom = AUTO_ID_ALPHABET.length
        for (i in 0 until size) {
            append(AUTO_ID_ALPHABET[rand.nextInt(maxRandom)])
        }
    }
}