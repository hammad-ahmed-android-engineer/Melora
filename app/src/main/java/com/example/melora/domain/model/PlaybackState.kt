package com.example.melora.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class PlaybackState(
    val currentSong: Song? = null,
    val isPlaying: Boolean = false,
    val currentPosition: Long = 0L,
    val duration: Long = 0L,
    val repeatMode: RepeatMode = RepeatMode.OFF,
    val isShuffleEnabled: Boolean = false,
    val queue: List<Song> = emptyList(),
    val currentIndex: Int = -1,
    val playbackSpeed: Float = 1.0f,
    val errorMessage: String? = null
)
