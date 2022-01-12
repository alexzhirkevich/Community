package github.alexzhirkevich.community.data.di

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import github.alexzhirkevich.community.MainActivity
import github.alexzhirkevich.community.MainApplication
import github.alexzhirkevich.community.core.entities.MediaContent
import github.alexzhirkevich.community.routing.Route
import github.alexzhirkevich.community.screens.chat.data.ChatDetailViewModel
import github.alexzhirkevich.community.screens.chat.data.ChatViewModel
import github.alexzhirkevich.community.screens.data.ChannelDetailViewModel
import github.alexzhirkevich.community.screens.data.ChannelViewModel
import github.alexzhirkevich.community.screens.fullscreen.data.FullscreenContentViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@EntryPoint
@InstallIn(ActivityComponent::class)
interface ViewModelFactoryProvider {
    val channelViewModelFactory : ChannelViewModel.Factory
    val chatViewModelFactory : ChatViewModel.Factory
    val channelDetailViewModelFactory : ChannelDetailViewModel.Factory
    val chatDetailViewModelFactory : ChatDetailViewModel.Factory
}

@ExperimentalCoroutinesApi
@ExperimentalPagerApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalStdlibApi
@Composable
internal fun channelViewModel(id : String) : ChannelViewModel {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        ViewModelFactoryProvider::class.java
    ).channelViewModelFactory

    val app = LocalContext.current.applicationContext as MainApplication

    return viewModel(
        key = id,
        factory = ChannelViewModel.provideFactory(
            factory,
            id,
            app.audioPlayer,
            app.storageRepository
        )
    )
}

@ExperimentalCoroutinesApi
@ExperimentalPagerApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalStdlibApi
@Composable
internal fun chatViewModel(id : String) : ChatViewModel {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        ViewModelFactoryProvider::class.java
    ).chatViewModelFactory

    val app = LocalContext.current.applicationContext as MainApplication


    return viewModel(key = id,
        factory = ChatViewModel.provideFactory(
            factory,
            id,
            app.audioPlayer,
            app.storageRepository
        ))
}

@ExperimentalCoroutinesApi
@ExperimentalPagerApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalStdlibApi
@Composable
internal fun channelDetailViewModel(id : String) : ChannelDetailViewModel {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        ViewModelFactoryProvider::class.java
    ).channelDetailViewModelFactory

    val app = LocalContext.current.applicationContext as MainApplication

    return viewModel(key = id,
        factory = ChannelDetailViewModel.provideFactory(
            factory,
            id,
            app.audioPlayer,
            app.storageRepository)
    )
}

@ExperimentalCoroutinesApi
@ExperimentalPagerApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalStdlibApi
@Composable
internal fun chatDetailViewModel(id : String) : ChatDetailViewModel {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        ViewModelFactoryProvider::class.java
    ).chatDetailViewModelFactory

    val app = LocalContext.current.applicationContext as MainApplication

    return viewModel(key = id,
        factory = ChatDetailViewModel.provideFactory(
            factory,
            id,
            app.audioPlayer,
            app.storageRepository)
    )
}

@FlowPreview
@ExperimentalCoroutinesApi
@ExperimentalStdlibApi
@Composable
internal fun fullscreenContentViewModel(content : List<MediaContent> ) : FullscreenContentViewModel {

    val app = LocalContext.current.applicationContext as MainApplication

    return viewModel(key = null,
    factory = FullscreenContentViewModel.Factory(
        content,app.storageRepository))
}

