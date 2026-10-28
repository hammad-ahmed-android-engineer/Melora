package com.example.melora.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class PlaybackQueue(
    val items: List<Song> = emptyList(),
    val currentIndex: Int = -1
)
