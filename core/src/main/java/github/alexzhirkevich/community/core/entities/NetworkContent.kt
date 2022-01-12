package github.alexzhirkevich.community.core.entities

sealed interface NetworkContent {
    val url : String
}

data class NetworkContentImpl(override val url: String): NetworkContent