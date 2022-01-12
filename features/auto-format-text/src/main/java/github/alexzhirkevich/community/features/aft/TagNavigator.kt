package github.alexzhirkevich.community.features.aft

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder

interface TagNavigator {
    suspend fun navigate(
        tag: String,
    )
}

object EmptyTagNavigator : TagNavigator{
    override suspend fun navigate(tag: String) {}
}