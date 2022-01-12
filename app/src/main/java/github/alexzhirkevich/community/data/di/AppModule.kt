package github.alexzhirkevich.community.data.di

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import github.alexzhirkevich.community.audioplayer.di.AudioPlayerModule
import github.alexzhirkevich.community.core.di.CoreBindModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalPagerApi
@ExperimentalCoroutinesApi
@FlowPreview
@ExperimentalStdlibApi
@InstallIn(SingletonComponent::class)
@Module(includes = [
    CoreBindModule::class,
    AudioPlayerModule::class
])
interface AppModule {

}


