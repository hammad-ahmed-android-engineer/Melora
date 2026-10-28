package com.example.melora.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class Album(
    val id: Long,
    val title: String,
    val artist: String,
    val songCount: Int = 0,
    val artworkUri: String? = null,
    val year: Int = 0
)
