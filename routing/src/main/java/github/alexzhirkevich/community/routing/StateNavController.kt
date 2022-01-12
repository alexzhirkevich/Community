package github.alexzhirkevich.community.routing

import android.content.Context
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.MainThread
import androidx.compose.animation.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.*
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.DialogNavigator
import com.google.accompanist.navigation.animation.AnimatedComposeNavigator
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext



@ExperimentalAnimationApi
@Composable
fun rememberStateNavController(
    animLen: Long,
    vararg navigators: Navigator<out NavDestination>
) : StateNavController {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    return rememberSaveable(inputs = navigators,
        saver = StateNavControllerSaver(context, scope,animLen)) {
        StateNavController(context, scope,animLen).apply {
            with(navigatorProvider) {
                addNavigator(ComposeNavigator())
                addNavigator(DialogNavigator())
                addNavigator(AnimatedComposeNavigator())
                navigators.forEach {
                    addNavigator(it)
                }
            }
        }
    }
}

private fun StateNavControllerSaver(
    context: Context,
    scope: CoroutineScope,
    animLen: Long
): Saver<StateNavController, *> = Saver(
    save = { it.saveState() },
    restore = { StateNavController(context,scope,animLen)
        .apply { restoreState(it) } }
)


class StateNavController(
    context: Context,

    private val scope : CoroutineScope,
    private val animLen : Long
) : NavHostController(context),CoroutineScope {

    val isEnabled : State<Boolean>
        get() = mIsEnabled

    override val coroutineContext: CoroutineContext = SupervisorJob()

    private val mIsEnabled = mutableStateOf(true)

    fun disableFor(ms : Long) {
        mIsEnabled.value = false
        scope.launch {
            try {
                delay(ms)
            } finally {
                mIsEnabled.value = true
            }
        }
    }

    init {
        addOnDestinationChangedListener { controller, destination, arguments ->
            disableFor(animLen)
        }
    }

    @MainThread
    override fun navigate(
        @IdRes resId: Int,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ){
        if (isEnabled.value){
            super.navigate(resId, args, navOptions, navigatorExtras)
            disableFor(animLen)
        }
    }

    @MainThread
    override fun navigate(
        request: NavDeepLinkRequest,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ){
        if (isEnabled.value){
            super.navigate(request, navOptions, navigatorExtras)
            disableFor(animLen)

        }
    }
}