package com.example.melora.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class EqualizerBand(
    val bandIndex: Short,
    val centerFreqHz: Int,
    val formattedFreq: String,
    val currentLevelMb: Short
)
