package com.example.melora.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class Song(
    val id: Long,
    val mediaUri: String,
    val title: String,
    val artist: String,
    val album: String,
    val albumArtist: String? = null,
    val albumId: Long = 0L,
    val artistId: Long = 0L,
    val duration: Long = 0L,
    val trackNumber: Int = 0,
    val year: Int = 0,
    val genre: String? = null,
    val artworkUri: String? = null,
    val filePath: String? = null,
    val fileSize: Long = 0L,
    val dateAdded: Long = 0L,
    val isFavorite: Boolean = false,
    val lastPlayed: Long = 0L,
    val playCount: Int = 0
)
