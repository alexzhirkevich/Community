package github.alexzhirkevich.community.bottomnavigation.selfprofile

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Tag
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.skydoves.landscapist.glide.GlideImage
import github.alexzhirkevich.community.bottomnavigation.R
import github.alexzhirkevich.community.bottomnavigation.selfprofile.data.SelfProfileViewModel
import github.alexzhirkevich.community.common.DataState
import github.alexzhirkevich.community.common.composable.*
import github.alexzhirkevich.community.common.valueOrNull
import github.alexzhirkevich.community.theme.Dimens
import github.alexzhirkevich.community.common.util.dateTime
import github.alexzhirkevich.community.core.entities.interfaces.User
import github.alexzhirkevich.community.features.aft.AutoFormatText
import github.alexzhirkevich.community.routing.Route
import github.alexzhirkevich.community.routing.navigate
import github.alexzhirkevich.community.routing.rememberTagNavigator
import github.alexzhirkevich.community.theme.Colors
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@Composable
internal fun SelfProfileScreen(
    navController: NavController,
    selfProfileViewModel: SelfProfileViewModel = hiltViewModel()
) {
    val user by selfProfileViewModel.data.collectAsState()
    when (val u = user) {
        is DataState.Success -> {

            val state = rememberCollapsingToolbarScaffoldState()
            val scrollState = rememberScrollState()
            CollapsingToolbarScaffold(
                modifier = Modifier.fillMaxSize(),
                state = state,
                scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
                toolbar = {
                    CollapsingChatAppBar(
                        imageUri = u.value.imageUri,
                        name = u.value.name,
                        description = stringResource(id = R.string.online),
                        toolbarState = state.toolbarState,
                        scrollState = scrollState
                    )
                },
            ) {
                Body(navController, selfProfileViewModel, scrollState)
            }
        }
    }
}

@Composable
private fun Body(
    navController: NavController,
    selfProfileViewModel: SelfProfileViewModel,
    scrollState: ScrollState
) {
    val user by selfProfileViewModel.data.collectAsState()

    Column(Modifier.verticalScroll(scrollState)) {
        when (val u = user) {
            is DataState.Success -> {
               MenuItems(
                   navController = navController,
                   viewModel = selfProfileViewModel,
                   user = u.value)
            }
            DataState.Loading -> {
//            Shimmer(baseColor = , highlightColor = )
            }
        }
    }
}

@Composable
private fun MenuItems(
    navController : NavController,
    viewModel: SelfProfileViewModel,
    user: User
) {
    val tagNavigator = rememberTagNavigator(
        navController = navController,
        taggableRepository = viewModel
    )

    Column() {
        if (user.tag.isNotBlank()) {
            DetailMenuItem(
                icon = Icons.Rounded.Tag,
                text = user.tag,
                label = stringResource(id = R.string.username),
                tagNavigator = tagNavigator,
                format = true,
                onClick = {
                    navController.navigate(
                        Route.EditProfileScreen(
                            Route.EditProfileScreen.Target.Username
                        )
                    )
                }
            )
        }
        if (user.description.isNotBlank()) {
            DetailMenuItem(
                icon = Icons.Rounded.Info,
                text = user.description,
                label = stringResource(id = R.string.description),
                tagNavigator = tagNavigator,
                format = true,
                onClick = {
                    navController.navigate(
                        Route.EditProfileScreen(
                            Route.EditProfileScreen.Target.Description
                        )
                    )
                }
            )
        }
    }
}

@Composable
private fun Shimmer(){
    Column(Modifier.fillMaxWidth()) {

    }
}


@Composable
private fun SelfProfileScreen_old(
    navController: NavController,
    selfProfileViewModel: SelfProfileViewModel = hiltViewModel()
) {

    val user by selfProfileViewModel.data.collectAsState()

    val density = LocalDensity.current

    val requestOptions = remember {
        RequestOptions()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .override(with(density) {
                150.dp.roundToPx()
            })
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        GlideImage(
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .border(
                    width = Dimens.AvatarBorderSize,
                    color = MaterialTheme.colors.surface,
                    shape = CircleShape
                ),
            imageModel = user.valueOrNull()?.imageUri,
            requestBuilder = {
                Glide.with(LocalView.current)
                    .asDrawable().thumbnail(.25f)
            },
            requestOptions = { requestOptions },

            loading = { AvatarPlaceHolder(user.valueOrNull()) },
            failure = { AvatarPlaceHolder(user.valueOrNull()) }
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = user.valueOrNull()?.name?.uppercase().orEmpty(),
            fontSize = Dimens.FontSizeLarge,
            fontWeight = FontWeight.Bold
        )

        val username = user.valueOrNull()?.tag.orEmpty()
        if (username.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "@$username",
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.secondary
            )
        }

        val desc = user.valueOrNull()?.description.orEmpty()
        if (desc.isNotEmpty()) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(id = R.string.about),
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.secondaryVariant
            )
            Spacer(modifier = Modifier.height(10.dp))
            AutoFormatText(
                linkColor = Colors.Link,
                text = desc,
                style = MaterialTheme.typography.body2,
                color = LocalTextStyle.current.color
            )
        }
        val online = user.valueOrNull()?.let {
            if (it.isOnline)
                stringResource(id = R.string.online)
            else it.lastOnline.dateTime().lastOnline
        }.orEmpty()
        if (online.isNotEmpty()){
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = online,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.secondaryVariant
            )
        }
        Spacer(modifier = Modifier.height(30.dp))
        Spacer(modifier = Modifier
            .height(1.dp)
            .clip(CircleShape)
            .padding(horizontal = 50.dp)
            .background(MaterialTheme.colors.surface)
            .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(30.dp))

        MenuButton(
            icon = Icons.Rounded.Edit,
            text = stringResource(id = R.string.edit_profile)) {
            navController.navigate(Route.EditProfileScreen)
        }

        Spacer(modifier = Modifier.height(10.dp))

        MenuButton(
            icon = Icons.Rounded.Settings,
            text = stringResource(id = R.string.settings)
        ) {

        }
    }
}

@Composable
private fun MenuButton(
    icon: ImageVector,
    text : String,
    onClick : () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .padding(horizontal = 50.dp)
            .fillMaxWidth()
    ) {
        Row {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = LocalTextStyle.current.color
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = text,
                fontSize = Dimens.FontSizeBig
            )
            Spacer(
                modifier = Modifier
                    .height(0.dp)
                    .weight(1f)
            )
        }
    }
}

@Composable
private fun AvatarPlaceHolder(user: User?) {
    AvatarTextPlaceholder(
        text = user?.name
            ?.toPlaceholderText().orEmpty(),
        color = Colors.Chats,
        fontSize = Dimens.FontSizeLarge,
        modifier = Modifier
            .fillMaxSize()
            .clip(CircleShape)
            .background(color = Colors.White)
    )
}