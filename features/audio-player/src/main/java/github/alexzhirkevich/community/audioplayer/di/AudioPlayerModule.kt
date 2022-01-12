package github.alexzhirkevich.community.audioplayer.di

import android.content.Context
import android.media.AudioAttributes
import dagger.Binds
import dagger.Module
import dagger.Provides
import github.alexzhirkevich.community.audioplayer.AudioPlayer
import github.alexzhirkevich.community.audioplayer.AudioPlayerImp
import kotlinx.coroutines.CoroutineScope

@Module
@Suppress("FunctionName")
class AudioPlayerModule {

    @Provides
    fun provideAudioPlayer(scope : CoroutineScope, context: Context) : AudioPlayer =
        AudioPlayerImp(context,scope)



//    @Provides
//    fun provideAudioProvider(): AudioPlayer =
//        AudioPlayerImp(AudioAttributes.CONTENT_TYPE_UNKNOWN)
//
//    @Provides
//    @AudioPlayer.MusicAudioProvider
//    fun provideAudioProvider_Music(): AudioPlayer =
//        AudioPlayerImp(AudioAttributes.CONTENT_TYPE_MUSIC)
//
//    @Provides
//    @AudioPlayer.VoiceAudioProvider
//    fun provideAudioProvider_Voice(): AudioPlayer =
//        AudioPlayerImp(AudioAttributes.CONTENT_TYPE_SPEECH)
}