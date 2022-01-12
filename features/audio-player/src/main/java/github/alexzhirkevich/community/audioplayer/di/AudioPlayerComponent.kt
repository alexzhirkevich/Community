package github.alexzhirkevich.community.audioplayer.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import github.alexzhirkevich.community.audioplayer.AudioPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton

@FlowPreview
@ExperimentalCoroutinesApi
@ExperimentalStdlibApi
@Component(modules = [AudioPlayerModule::class])
@Singleton
interface AudioPlayerComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun setScope(coroutineScope: CoroutineScope) : Builder

        @BindsInstance
        fun setContext(context: Context) : Builder

        fun build() : AudioPlayerComponent
    }

    val audioPlayer : AudioPlayer
}