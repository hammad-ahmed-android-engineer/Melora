package com.example.melora.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class EqualizerState(
    val isAvailable: Boolean = false,
    val isEnabled: Boolean = true,
    val bands: List<EqualizerBand> = emptyList(),
    val minLevelMb: Short = (-1500).toShort(),
    val maxLevelMb: Short = 1500.toShort(),
    val presets: List<String> = emptyList(),
    val currentPreset: Short = (-1).toShort(),
    val bassBoostStrength: Int = 0,
    val virtualizerStrength: Int = 0
)
