package github.alexzhirkevich.community.bottomnavigation

import androidx.annotation.StringRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import github.alexzhirkevich.community.bottomnavigation.chats.ChatsViewPagerScreen
import github.alexzhirkevich.community.bottomnavigation.selfprofile.SelfProfileScreen
import github.alexzhirkevich.community.theme.Dimens
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


private sealed class BottomNavigationScreens (
    val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    object Events : BottomNavigationScreens("Events", R.string.events, Icons.Filled.Place)
    object Calls : BottomNavigationScreens("Calls", R.string.calls, Icons.Filled.Call)
    object Chats : BottomNavigationScreens("Chats", R.string.chats, Icons.Filled.Email)
    object Profile : BottomNavigationScreens("Profile", R.string.profile, Icons.Filled.Person)
}

@FlowPreview
@ExperimentalCoroutinesApi
@ExperimentalStdlibApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
fun BottomNavigationScreen(
    navHostController: NavController,
) {

    val bottomNavigationController = rememberNavController()

    val items = listOf(
        BottomNavigationScreens.Events,
        BottomNavigationScreens.Calls,
        BottomNavigationScreens.Chats,
        BottomNavigationScreens.Profile,
    )

    Scaffold(
        bottomBar = {
            BottomNavigation(
                navController = bottomNavigationController,
                items = items,
            )
        }
    ) {

        Box(
            Modifier.padding(
                top = it.calculateTopPadding(),
                bottom = it.calculateBottomPadding()
            )
        ) {
            NavHost(
                bottomNavigationController = bottomNavigationController,
                mainNavController = navHostController,
            )
        }
    }
}

@ExperimentalCoroutinesApi
@FlowPreview
@ExperimentalStdlibApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
private fun NavHost(
    bottomNavigationController: NavHostController,
    mainNavController : NavController,
) {

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current)

    androidx.navigation.compose.NavHost(
        navController = bottomNavigationController,
        startDestination = BottomNavigationScreens.Chats.route
    ) {
        composable(BottomNavigationScreens.Events.route) {
            CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {

            }
        }
        composable(BottomNavigationScreens.Calls.route) {
            CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {

            }
        }
        composable(BottomNavigationScreens.Chats.route) {
            CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                ChatsViewPagerScreen(mainNavController)
            }
        }
        composable(BottomNavigationScreens.Profile.route) {
            CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                SelfProfileScreen(mainNavController)
            }
        }
    }
}

@Composable
private fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

@Composable
private fun BottomNavigation(
    navController: NavHostController,
    items: List<BottomNavigationScreens>,
) {
    BottomNavigation {
        val currentRoute = currentRoute(navController)
        items.forEach { screen ->
            BottomNavigationItem(
                modifier = Modifier.background(
                    color = MaterialTheme.colors.background
                ),
                icon = {
                    Icon(
//                        modifier = Modifier.size(
//                            if (currentRoute == screen.route)
//                                25.dp
//                        else 20.dp
//                        ),
                        tint = if (currentRoute == screen.route)
                            MaterialTheme.colors.surface
                        else MaterialTheme.colors.secondaryVariant,
                        imageVector = screen.icon,
                        contentDescription = null)
                       },
                alwaysShowLabel = true,
                label = {
                    Text(
                        text = stringResource(id = screen.resourceId),
//                        fontSize = if (currentRoute == screen.route)
//                            Dimens.FontSizeMedium
//                        else Dimens.FontSizeSmall
                    )
                },
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }

                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}