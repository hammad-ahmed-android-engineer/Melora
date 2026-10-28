package com.example.melora.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class Artist(
    val id: Long,
    val name: String,
    val songCount: Int = 0,
    val albumCount: Int = 0,
    val artworkUri: String? = null
)
