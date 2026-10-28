package com.example.melora.core.utils

import java.util.Locale

/**
 * Extension function to format bytes into a human-readable size string (KB, MB, GB).
 */
fun Long.formatFileSize(): String {
    if (this <= 0) return "0 KB"
    val kb = this / 1024.0
    val mb = kb / 1024.0
    val gb = mb / 1024.0

    return when {
        gb >= 1.0 -> String.format(Locale.getDefault(), "%.2f GB", gb)
        mb >= 1.0 -> String.format(Locale.getDefault(), "%.1f MB", mb)
        else -> String.format(Locale.getDefault(), "%.0f KB", kb)
    }
}
