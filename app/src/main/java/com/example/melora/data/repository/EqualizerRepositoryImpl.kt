package com.example.melora.data.repository

import com.example.melora.core.media.EqualizerManager
import com.example.melora.domain.model.EqualizerState
import com.example.melora.domain.repository.EqualizerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EqualizerRepositoryImpl @Inject constructor(
    private val equalizerManager: EqualizerManager
) : EqualizerRepository {

    private val _equalizerState = MutableStateFlow(EqualizerState())
    override val equalizerState: StateFlow<EqualizerState> = _equalizerState.asStateFlow()

    init {
        equalizerManager.ensureInitialized()
        refreshState()
    }

    override fun refreshState() {
        val available = equalizerManager.isAvailable()
        val range: Pair<Short, Short> = if (available) equalizerManager.getBandLevelRange() else Pair((-1500).toShort(), 1500.toShort())

        _equalizerState.value = EqualizerState(
            isAvailable = available,
            isEnabled = if (available) equalizerManager.isEnabled() else true,
            bands = if (available) equalizerManager.getBands() else emptyList(),
            minLevelMb = range.first,
            maxLevelMb = range.second,
            presets = if (available) equalizerManager.getPresets() else emptyList(),
            currentPreset = if (available) equalizerManager.getCurrentPreset() else (-1).toShort(),
            bassBoostStrength = if (available) equalizerManager.getBassBoostStrength() else 0,
            virtualizerStrength = if (available) equalizerManager.getVirtualizerStrength() else 0
        )
    }

    override fun setEnabled(enabled: Boolean) {
        equalizerManager.setEnabled(enabled)
        refreshState()
    }

    override fun setBandLevel(band: Short, levelMb: Short) {
        equalizerManager.setBandLevel(band, levelMb)
        refreshState()
    }

    override fun applyPreset(presetIndex: Short) {
        equalizerManager.applyPreset(presetIndex)
        refreshState()
    }

    override fun setBassBoost(strength: Int) {
        equalizerManager.setBassBoost(strength)
        refreshState()
    }

    override fun setVirtualizer(strength: Int) {
        equalizerManager.setVirtualizer(strength)
        refreshState()
    }
}
