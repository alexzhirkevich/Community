package github.alexzhirkevich.community.ui.widgets.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi

sealed class Routes(val route : String) {

    object AuthScreen : Routes("auth")
    object BottomNavigationScreen : Routes("bottom_navigation")
    class  ChannelScreen(private val id: String)
        : Routes("channel/$id")
}



fun NavController.navigate(routes: Routes, options : NavOptionsBuilder.() -> Unit = {}) =
    navigate(routes.route,options)

@ExperimentalStdlibApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
fun MainScreen(){

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.BottomNavigationScreen.route){
        composable(Routes.AuthScreen.route) {
            AuthScreen(navController = navController)
        }
        composable(Routes.BottomNavigationScreen.route) {
            BottomNavigationScreen(navHostController = navController)
        }
        composable(Routes.ChannelScreen("{id}").route,
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
            })
        ) {
            ChannelScreen(
                navHostController = navController,
                id = it.arguments?.getString("id").orEmpty()
            )
        }
    }
}