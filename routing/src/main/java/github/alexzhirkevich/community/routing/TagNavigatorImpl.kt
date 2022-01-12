package github.alexzhirkevich.community.routing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import github.alexzhirkevich.community.core.Response
import github.alexzhirkevich.community.core.entities.interfaces.Channel
import github.alexzhirkevich.community.core.entities.interfaces.User
import github.alexzhirkevich.community.core.repo.interfaces.TaggableRepository
import github.alexzhirkevich.community.features.aft.TagNavigator
import kotlinx.coroutines.flow.first

@Composable
fun rememberTagNavigator(
    navController: NavController,
    taggableRepository: TaggableRepository
) : TagNavigator = remember {
    TagNavigatorImpl(navController,taggableRepository)
}

class TagNavigatorImpl constructor(
    private val navController: NavController,
    private val taggableRepository: TaggableRepository
) : TagNavigator {

    private var inProgress: Boolean = false

    override suspend fun navigate(
        tag: String,
    ) {
        if (!inProgress) {
            inProgress = true
            try {
                val taggable = taggableRepository.get(tag).first()
                if (taggable is Response.Success)
                    when (val v = taggable.value) {
                        is User -> TODO("navigate to user profile")
                        is Channel -> navController.navigate(
                            Route.ChannelDetailScreen(v.id)
                        )
                    }
            } finally {
                inProgress = false
            }
        }
    }
}