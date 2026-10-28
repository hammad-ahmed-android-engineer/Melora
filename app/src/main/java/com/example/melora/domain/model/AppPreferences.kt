package com.example.melora.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class AppPreferences(
    val themeMode: String = "system",
    val defaultTab: String = "home",
    val resumePreviousSession: Boolean = true,
    val rememberPosition: Boolean = true,
    val lastSongId: Long? = null,
    val lastPosition: Long = 0L
)
