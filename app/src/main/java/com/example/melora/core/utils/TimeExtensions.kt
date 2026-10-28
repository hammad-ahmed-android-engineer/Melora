package com.example.melora.core.utils

import java.util.Locale

/**
 * Extension function to format duration in milliseconds into a mm:ss or hh:mm:ss string representation.
 */
fun Long.formatTimeMs(): String {
    val totalSeconds = (this / 1000).coerceAtLeast(0)
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return if (hours > 0) {
        String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format(Locale.getDefault(), "%d:%02d", minutes, seconds)
    }
}

/**
 * Extension function to format duration in seconds into a mm:ss or hh:mm:ss string representation.
 */
fun Long.formatTimeSeconds(): String = (this * 1000L).formatTimeMs()
