package com.example.melora.domain.repository

import com.example.melora.domain.model.EqualizerState
import kotlinx.coroutines.flow.StateFlow

interface EqualizerRepository {
    val equalizerState: StateFlow<EqualizerState>
    fun setEnabled(enabled: Boolean)
    fun setBandLevel(band: Short, levelMb: Short)
    fun applyPreset(presetIndex: Short)
    fun setBassBoost(strength: Int)
    fun setVirtualizer(strength: Int)
    fun refreshState()
}
