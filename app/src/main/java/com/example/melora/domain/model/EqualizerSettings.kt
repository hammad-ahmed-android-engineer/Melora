package com.example.melora.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class EqualizerSettings(
    val isEnabled: Boolean = false,
    val presetIndex: Int = -1,
    val bandLevels: Map<Int, Int> = emptyMap(),
    val bassBoostStrength: Int = 0,
    val virtualizerStrength: Int = 0
)
