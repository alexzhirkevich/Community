package github.alexzhirkevich.community

import android.util.Log
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.*
import androidx.compose.animation.core.InternalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import github.alexzhirkevich.community.bottomnavigation.BottomNavigationScreen
import github.alexzhirkevich.community.data.di.*
import github.alexzhirkevich.community.routing.Route
import github.alexzhirkevich.community.routing.contains
import github.alexzhirkevich.community.screens.ChannelDetailScreen
import github.alexzhirkevich.community.screens.ChannelScreen
import github.alexzhirkevich.community.screens.auth.AuthScreen
import github.alexzhirkevich.community.screens.chat.ChatDetailScreen
import github.alexzhirkevich.community.screens.chat.ChatScreen
import github.alexzhirkevich.community.screens.fullscreen.FullscreenContentScreen
import github.alexzhirkevich.community.screens.input.profile.EditProfileScreen
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@InternalAnimationApi
@ExperimentalComposeUiApi
@FlowPreview
@ExperimentalCoroutinesApi
@ExperimentalStdlibApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
fun MainScreen() {

    val navController = rememberAnimatedNavController()

    navController.setLifecycleOwner(LocalLifecycleOwner.current)
    LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher?.let {
        navController.setOnBackPressedDispatcher(it)
    }

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current)

    AnimatedNavHost(
        navController = navController,
        startDestination = Route.BottomNavigationScreen.route
    ) {

        composable(
            Route.AuthScreen.route
        ) {
            CompositionLocalProvider(
                LocalViewModelStoreOwner provides viewModelStoreOwner
            ) {
                AuthScreen(navController = navController)
            }
        }
        animatedComposable(
            Route.BottomNavigationScreen,
        ) {
            CompositionLocalProvider(
                LocalViewModelStoreOwner provides viewModelStoreOwner
            ) {
                BottomNavigationScreen(
                    navHostController = navController,
                )
            }
        }
        animatedComposable(Route.EditProfileScreen){
            CompositionLocalProvider(
                LocalViewModelStoreOwner provides viewModelStoreOwner
            ) {
                val route = Route.EditProfileScreen.from(it)
                EditProfileScreen(
                    navController = navController,
                    target = route?.target ?: Route.EditProfileScreen.Target.Name
                )
            }
        }
        animatedComposable(
            Route.ChannelScreen,
        ) {
            CompositionLocalProvider(
                LocalViewModelStoreOwner provides viewModelStoreOwner
            ) {
                val id = Route.ChannelScreen.getArguments(it)
                ChannelScreen(
                    navHostController = navController,
                    viewModel = channelViewModel(id = id),
                )
            }
        }
        animatedComposable(
            Route.ChannelDetailScreen
        ) {
            CompositionLocalProvider(
                LocalViewModelStoreOwner provides viewModelStoreOwner
            ) {
                val id = Route.ChannelDetailScreen.getArguments(it)

                ChannelDetailScreen(
                    navController = navController,
                    channelDetailViewModel = channelDetailViewModel(id = id),
                )
            }
        }
        animatedComposable(
            Route.ChatScreen,
            exitTransition = {
                Log.e("qwe",targetState.destination.route.orEmpty())

                if (Route.GroupDetailScreen in targetState.destination)
                    ExitTransition.None
                else
                    slideOutOfContainer(AnimatedContentScope.SlideDirection.Left)
            }
        ) {

            CompositionLocalProvider(
                LocalViewModelStoreOwner provides viewModelStoreOwner
            ) {

                val id = Route.ChatScreen.getArguments(it)
                val vm = chatViewModel(id = id)
                ChatScreen(
                    navHostController = navController,
                    vm
                )
            }
        }
        animatedComposable(
            Route.GroupDetailScreen,
            enterTransition ={
                EnterTransition.None
            },
        ) {
            CompositionLocalProvider(
                LocalViewModelStoreOwner provides viewModelStoreOwner
            ) {
                val id = Route.GroupDetailScreen.getArguments(it)

                ChatDetailScreen(
                    navController = navController,
                    chatDetailViewModel = chatDetailViewModel(id = id),
                )
            }
        }
        animatedComposable(
            Route.FullscreenContentScreen,
        ) {


            CompositionLocalProvider(
                LocalViewModelStoreOwner provides viewModelStoreOwner
            ) {

                val content = Route.FullscreenContentScreen.getArguments(it)
                val vm = fullscreenContentViewModel(content = content)

                FullscreenContentScreen(vm)
            }
        }
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.animatedComposable(
    route: Route,
    deepLinks: List<NavDeepLink> = emptyList(),
    enterTransition: (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition?)? = {
        slideIntoContainer(AnimatedContentScope.SlideDirection.Left)
    },
    exitTransition: (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?)? ={
        slideOutOfContainer(AnimatedContentScope.SlideDirection.Left)
    },
    popEnterTransition: (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition?)? = {
        slideIntoContainer(AnimatedContentScope.SlideDirection.Right)
    },
    popExitTransition: (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?)? = {
        slideOutOfContainer(AnimatedContentScope.SlideDirection.Right)

    },
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route.route,
        route.navArguments,
        deepLinks,
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        popEnterTransition = popEnterTransition,
        popExitTransition = popExitTransition,
        content = content
    )
}

private fun getRoute(navBackStackEntry: NavBackStackEntry){
    navBackStackEntry.arguments
}

