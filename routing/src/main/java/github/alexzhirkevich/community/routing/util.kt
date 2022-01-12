package github.alexzhirkevich.community.routing

import androidx.navigation.*

fun NavController.navigate(
    route: Route,
    options : NavOptionsBuilder.() -> Unit = {}) {

    navigate(route.route,options)
}

fun NavController.popToRoute(route: Route, inclusive: Boolean) {

    if (!popBackStack(route.route, inclusive)) {
        popBackStack()
        navigate(route.route)
      //  this.disableFor(animLen)
    }
}