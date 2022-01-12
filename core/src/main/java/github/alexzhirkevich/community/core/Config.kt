package github.alexzhirkevich.community.core

object Config {
    const val NameMinLength = 3
    const val NameMaxLength = 30

    const val TagMinLength = 5
    const val TagMaxLength = 25

    const val DescriptionMaxLength = 150

    val TagSymbols: Set<Char> by lazy {
        "abcdefghijklmnopqrstuvwxyz0123456789_".toCharArray().toSet()
    }
}