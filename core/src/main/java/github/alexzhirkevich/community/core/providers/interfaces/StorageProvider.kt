package github.alexzhirkevich.community.core.providers.interfaces

import android.graphics.Bitmap
import android.net.Uri
import com.bumptech.glide.request.RequestOptions
import github.alexzhirkevich.community.core.Loading
import github.alexzhirkevich.community.core.providers.base.Provider
import kotlinx.coroutines.flow.Flow



interface StorageProvider : Provider{


    fun uploadImage(path: Uri) : Flow<Loading<Uri>>


    fun uploadVoice(path: Uri) : Flow<Loading<Uri>>

}