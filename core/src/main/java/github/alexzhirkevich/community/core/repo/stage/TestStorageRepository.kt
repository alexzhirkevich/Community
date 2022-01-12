package github.alexzhirkevich.community.core.repo.stage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import dagger.hilt.android.qualifiers.ApplicationContext
import github.alexzhirkevich.community.core.Loading
import github.alexzhirkevich.community.core.entities.interfaces.Chat
import github.alexzhirkevich.community.core.entities.interfaces.User
import github.alexzhirkevich.community.core.repo.interfaces.StorageChatUploader
import github.alexzhirkevich.community.core.repo.interfaces.StorageRepository
import github.alexzhirkevich.community.core.repo.interfaces.StorageUserUploader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.roundToLong
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import github.alexzhirkevich.community.core.entities.NetworkContent
import github.alexzhirkevich.community.core.repo.imp.storage.ContentType
import github.alexzhirkevich.community.core.repo.imp.storage.toContentType
import kotlinx.coroutines.flow.asStateFlow


@Singleton
class TestStorageRepository @Inject constructor(
    private val context: Context
): StorageRepository {

    override fun uploadForChat(chat: Chat): StorageChatUploader {
        TODO("Not yet implemented")
    }

    override fun uploadForUser(userId: String): StorageUserUploader {
        TODO("Not yet implemented")
    }

    override suspend fun getUserAvatars(user: User, limit: Int): Collection<Uri> {
        TODO("Not yet implemented")
    }

    override fun isDownloaded(content: NetworkContent): Uri? {
        val dir = File(context.getExternalFilesDir(null), content.toContentType().directory)
        val file = File(dir, content.url.hashCode().toString())
        return if (file.exists())
            file.toUri()
        else null
    }

    override fun isDownloading(url: String): Boolean = false


    override fun cancelDownload(url: String): Boolean {
        return false
    }


    override fun downloadVoice(url: String): StateFlow<Loading<Uri>> {
        TODO("Not yet implemented")
    }

    override fun downloadVideo(url: String): StateFlow<Loading<Uri>> {
        TODO("Not yet implemented")
    }

    override fun cancelUpload(path: Uri): Boolean {
        TODO("Not yet implemented")
    }

    override fun downloadImage(url: String): StateFlow<Loading<Uri>> {

        val stateFlow = MutableStateFlow<Loading<Uri>>(Loading.Progress<Uri>(0f,0))

        val dir = File(context.getExternalFilesDir(null), ContentType.Image.directory)

        val file = File(dir,url.hashCode().toString())

        if (file.exists()){
            stateFlow.tryEmit(Loading.Success(file.toUri()))
            return stateFlow.asStateFlow()
        }

        Glide.with(context).asBitmap().load(url)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .addListener(object  : RequestListener<Bitmap>{

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {
                    e?.let { stateFlow.tryEmit(Loading.Error(it)) }
                    return false
                }

                override fun onResourceReady(
                    resource: Bitmap,
                    model: Any?,
                    target: Target<Bitmap>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    try {

                        dir.mkdirs()

                        FileOutputStream(file, false).use {
                            resource.compress(Bitmap.CompressFormat.JPEG, 100, it)
                        }
                        stateFlow.tryEmit(Loading.Success(file.toUri()))
                        return true
                    }catch (t : Throwable){
                        println(t)
                    }
                    return false
                }


            }).submit()

        GlobalScope.launch {
            repeat(10){

                if (stateFlow.value !is Loading.Success)
                    stateFlow.value = (Loading.Progress<Uri>(it.toFloat()/10,(2.43*1024*1024).roundToLong()))
                delay(200)
            }
        }

        return stateFlow
    }

}