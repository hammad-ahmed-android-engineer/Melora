package com.example.melora.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class AudioFolder(
    val path: String,
    val name: String,
    val songCount: Int
)
