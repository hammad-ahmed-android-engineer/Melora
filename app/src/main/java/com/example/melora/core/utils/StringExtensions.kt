package com.example.melora.core.utils

import java.util.Locale

/**
 * Extension function to capitalize the first character of a string.
 */
fun String.capitalizeFirst(): String {
    return replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}
