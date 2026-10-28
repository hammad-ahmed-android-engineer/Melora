package com.example.melora.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class Playlist(
    val id: Long = 0L,
    val name: String,
    val createdAt: Long = System.currentTimeMillis(),
    val songCount: Int = 0,
    val artworkUri: String? = null
)

@Immutable
data class PlaylistSong(
    val playlistId: Long,
    val songId: Long,
    val orderIndex: Int
)
