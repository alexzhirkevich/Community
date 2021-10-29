package github.alexzhirkevich.community.ui.widgets.screens

import androidx.annotation.StringRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import github.alexzhirkevich.community.R
import github.alexzhirkevich.community.ui.widgets.chats.ChatsViewPagerWidget


private sealed class BottomNavigationScreens (
    val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    object Events : BottomNavigationScreens("Events", R.string.events, Icons.Filled.Place)
    object Calls : BottomNavigationScreens("Calls", R.string.calls, Icons.Filled.Call)
    object Chats : BottomNavigationScreens("Chats", R.string.chats, Icons.Filled.Email)
    object Profile : BottomNavigationScreens("Profile", R.string.profile, Icons.Filled.Person)
}

@ExperimentalStdlibApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
fun BottomNavigationScreen(navHostController: NavHostController) {

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
                items = items
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
                mainNavController = navHostController
            )
        }
    }
}

@ExperimentalStdlibApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
private fun NavHost(
    bottomNavigationController: NavHostController,
    mainNavController : NavHostController
) {



    NavHost(bottomNavigationController, startDestination = BottomNavigationScreens.Chats.route) {
        composable(BottomNavigationScreens.Events.route) {
        }
        composable(BottomNavigationScreens.Calls.route) {

        }
        composable(BottomNavigationScreens.Chats.route) {
            ChatsViewPagerWidget(mainNavController)
        }
        composable(BottomNavigationScreens.Profile.route) {
        }
    }
}

@Composable
private fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

@Composable
private fun BottomNavigation(
    navController: NavHostController,
    items : List<BottomNavigationScreens>
) {
    BottomNavigation {
        val currentRoute = currentRoute(navController)
        items.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(imageVector = screen.icon, contentDescription = null)},
                label = { Text(stringResource(id = screen.resourceId)) },
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