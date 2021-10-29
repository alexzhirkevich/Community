package github.alexzhirkevich.community.common.util

val String.isLatinDigitOrUnderscore : Boolean
    get() = all { it == '_' || it.isDigit() || it in 'a'..'z' || it in 'A'..'Z' }