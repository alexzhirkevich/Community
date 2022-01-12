package github.alexzhirkevich.community

import android.app.Application
import android.media.MediaPlayer
import dagger.hilt.android.HiltAndroidApp
import github.alexzhirkevich.community.audioplayer.AudioPlayer
import github.alexzhirkevich.community.audioplayer.di.DaggerAudioPlayerComponent
import github.alexzhirkevich.community.core.di.CoreComponent
import github.alexzhirkevich.community.core.di.DaggerCoreComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.GlobalScope
import javax.inject.Provider

@HiltAndroidApp
@ExperimentalStdlibApi
class MainApplication : Application() {

    @ExperimentalCoroutinesApi
    @FlowPreview
    private val coreComponent : CoreComponent by lazy {
        DaggerCoreComponent.builder()
            .setScope(GlobalScope)
            .setContext(applicationContext)
            .build()
    }

    val audioPlayer : AudioPlayer by lazy {
        DaggerAudioPlayerComponent.builder()
            .setScope(GlobalScope)
            .setContext(applicationContext)
            .build()
            .audioPlayer
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    @ExperimentalStdlibApi
    val storageRepository
        get() = coreComponent.storageRepository

}