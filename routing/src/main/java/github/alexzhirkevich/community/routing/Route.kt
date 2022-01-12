package github.alexzhirkevich.community.routing

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import github.alexzhirkevich.community.core.entities.MediaContent
import java.io.Serializable

private interface RouteCreator<R : Route> {
    fun from(entry: NavBackStackEntry) : R?
}

operator fun NavDestination.contains(route: Route) : Boolean{
    return this.route?.startsWith(route.baseName) == true
}

sealed class Route(val route : String, val baseName : String) : Serializable {

    open val navArguments : List<NamedNavArgument> = emptyList()

    open val enterTransition : EnterTransition = EnterTransition.None
    open val popExitTransition : ExitTransition = ExitTransition.None
    open val targetExitTransition : ExitTransition = ExitTransition.None
    open val targetPopEnterTransition : EnterTransition = EnterTransition.None

    open fun getArguments(entry : NavBackStackEntry) : Any? = null


    object AuthScreen : Route("auth","auth"), RouteCreator<AuthScreen> {

        override fun from(entry: NavBackStackEntry): AuthScreen? =
            if (entry.destination.route == "auth") this else null
    }

    object BottomNavigationScreen : Route("bottom_navigation","bottom_navigation"),
        RouteCreator<BottomNavigationScreen>{

        override fun from(entry: NavBackStackEntry): BottomNavigationScreen? =
            if (entry.destination.route == "bottom_navigation") this else null
    }

    open class EditProfileScreen(val target : Target) : Route("edit_profile/${target}","edit_profile"){

        sealed class Target(private val repr : String) {
            object Name : Target("name")
            object Username : Target("username")
            object Description : Target("description")

            override fun toString(): String = repr

            override fun hashCode(): Int = repr.hashCode()
        }


        private object None : Target("{$ARG_TARGET}")

        override fun getArguments(entry: NavBackStackEntry) =
            entry.arguments?.getString(ARG_TARGET).orEmpty()


        companion object : EditProfileScreen(None), RouteCreator<EditProfileScreen> {

            private const val ARG_TARGET = "target"

            override fun from(entry: NavBackStackEntry): EditProfileScreen? {
                return if (entry.destination.route?.startsWith("edit_profile/")==true){
                    when (getArguments(entry)){
                        Target.Name.toString() -> EditProfileScreen(Target.Name)
                        Target.Username.toString() -> EditProfileScreen(Target.Username)
                        Target.Description.toString() -> EditProfileScreen(Target.Description)
                        else -> null
                    }
                } else null
            }
        }
    }
    open class ChannelScreen(private val id: String)
        : Route("channel/$id","channel") {
        override val enterTransition: EnterTransition
            get() = SlideInFromRight
        override val popExitTransition: ExitTransition
            get() = SlideOutToRight
        override val targetExitTransition: ExitTransition
            get() = SlideOutToLeft
        override val targetPopEnterTransition: EnterTransition
            get() = SlideInFromLeft


        override fun getArguments(entry: NavBackStackEntry) =
            entry.arguments?.getString(ARG_ID).orEmpty()

        companion object : ChannelScreen("{$ARG_ID}"), RouteCreator<ChannelScreen> {
            override fun from(entry: NavBackStackEntry): ChannelScreen? {
                return if (entry.destination.route?.startsWith("channel/")==true){
                    ChannelScreen(getArguments(entry))
                } else null
            }
        }

        override val navArguments: List<NamedNavArgument>
            get() = listOf(navArgument(ARG_ID) {
                type = NavType.StringType
            })
    }
    open class  ChannelDetailScreen(id: String)
        : Route("channelDetail/$id","channelDetail") {


        override val navArguments: List<NamedNavArgument>
            get() = listOf(navArgument(ARG_ID) {
                type = NavType.StringType
            })

        override fun getArguments(entry: NavBackStackEntry) =
            entry.arguments?.getString(ARG_ID).orEmpty()

        companion object : ChannelDetailScreen("{$ARG_ID}"), RouteCreator<ChannelDetailScreen> {
            override fun from(entry: NavBackStackEntry): ChannelDetailScreen? {
                return if (entry.destination.route?.startsWith("channelDetail/") == true) {
                    ChannelDetailScreen(getArguments(entry))
                } else null
            }
        }
    }

    open class ChatScreen(id: String)
        : Route("chat/$id","chat") {
        override val enterTransition: EnterTransition
            get() = SlideInFromRight
        override val popExitTransition: ExitTransition
            get() = SlideOutToLeft
        override val targetExitTransition: ExitTransition
            get() = SlideOutToRight
        override val targetPopEnterTransition: EnterTransition
            get() = SlideInFromLeft

        override val navArguments: List<NamedNavArgument>
            get() = listOf(navArgument("id") {
                type = NavType.StringType
            })

        override fun getArguments(entry: NavBackStackEntry) =
            entry.arguments?.getString(ARG_ID).orEmpty()

        companion object : ChatScreen("{$ARG_ID}"), RouteCreator<ChatScreen> {
            override fun from(entry: NavBackStackEntry): ChatScreen? {
                return if (entry.destination.route?.startsWith("chat/") == true) {
                    ChatScreen(getArguments(entry))
                } else null
            }
        }
    }

    open class  GroupDetailScreen(id: String)
        : Route("chatDetail/$id","chatDetail") {


        override val navArguments: List<NamedNavArgument>
            get() = listOf(navArgument(ARG_ID) {
                type = NavType.StringType
            })

        override fun getArguments(entry: NavBackStackEntry) =
            entry.arguments?.getString(ARG_ID).orEmpty()

        companion object : GroupDetailScreen("{$ARG_ID}"), RouteCreator<GroupDetailScreen> {
            override fun from(entry: NavBackStackEntry): GroupDetailScreen? {
                return if (entry.destination.route?.startsWith("chatDetail/") == true) {
                    GroupDetailScreen(getArguments(entry))
                } else null
            }
        }
    }

    open class FullscreenContentScreen(content : List<MediaContent>)
        : Route(
        if(content.isNotEmpty())
            "fullscreen/{${gson.toJson(content)}}"
        else "fullscreen/{$ARG_CONTENT}",
        "fullscreen"
    ) {

        override val navArguments: List<NamedNavArgument>
        get() = listOf(navArgument(ARG_CONTENT) {
            type = NavType.StringType
            defaultValue = gson.toJson(listOf<MediaContent>())
        })

        override fun getArguments(entry: NavBackStackEntry): List<MediaContent> =
            entry.arguments?.getString(ARG_CONTENT)?.let {
                gson.fromJson(it, type)
            } ?: emptyList()

        companion object : FullscreenContentScreen(emptyList()) {

            private const val ARG_CONTENT = "content"
            private val type = object : TypeToken<List<MediaContent>>() {}.type
        }
    }

    companion object : RouteCreator<Route> {

        private val creators : List<RouteCreator<*>> by lazy {
            listOf(
                AuthScreen,
                BottomNavigationScreen,
                EditProfileScreen,
                ChannelScreen.Companion,
                ChannelDetailScreen.Companion,
                ChatScreen.Companion,
            )
        }
        override fun from(entry: NavBackStackEntry): Route? =
            creators.mapNotNull {
                it.from(entry)
            }.firstOrNull()

        private val gson = Gson()
        private const val ARG_ID = "id"
    }
}