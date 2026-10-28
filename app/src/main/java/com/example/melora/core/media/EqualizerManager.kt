package com.example.melora.core.media

import android.media.audiofx.BassBoost
import android.media.audiofx.Equalizer
import android.media.audiofx.Virtualizer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.melora.domain.model.EqualizerBand
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EqualizerManager @Inject constructor() {

    private var equalizer: Equalizer? = null
    private var bassBoost: BassBoost? = null
    private var virtualizer: Virtualizer? = null

    private var isEnabledState = true
    private var currentSessionId: Int = 0

    private val _isAvailableFlow = MutableStateFlow(false)
    val isAvailableFlow: StateFlow<Boolean> = _isAvailableFlow.asStateFlow()

    fun initAudioSession(audioSessionId: Int) {
        if (equalizer != null && currentSessionId == audioSessionId && audioSessionId != 0) {
            return
        }
        release()
        currentSessionId = audioSessionId
        try {
            equalizer = Equalizer(0, audioSessionId).apply { enabled = isEnabledState }
            bassBoost = BassBoost(0, audioSessionId).apply { enabled = isEnabledState }
            virtualizer = Virtualizer(0, audioSessionId).apply { enabled = isEnabledState }
            _isAvailableFlow.value = true
        } catch (e: Exception) {
            e.printStackTrace()
            if (audioSessionId != 0) {
                try {
                    currentSessionId = 0
                    equalizer = Equalizer(0, 0).apply { enabled = isEnabledState }
                    bassBoost = BassBoost(0, 0).apply { enabled = isEnabledState }
                    virtualizer = Virtualizer(0, 0).apply { enabled = isEnabledState }
                    _isAvailableFlow.value = true
                } catch (e2: Exception) {
                    e2.printStackTrace()
                    _isAvailableFlow.value = false
                }
            } else {
                _isAvailableFlow.value = false
            }
        }
    }

    fun ensureInitialized() {
        if (equalizer == null) {
            initAudioSession(0)
        }
    }

    fun isAvailable(): Boolean {
        if (equalizer == null) {
            ensureInitialized()
        }
        return equalizer != null
    }

    fun isEnabled(): Boolean = equalizer?.enabled ?: false

    fun setEnabled(enabled: Boolean) {
        isEnabledState = enabled
        try {
            equalizer?.enabled = enabled
            bassBoost?.enabled = enabled
            virtualizer?.enabled = enabled
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getBands(): List<EqualizerBand> {
        val eq = equalizer ?: return emptyList()
        val numBands = eq.numberOfBands
        val list = mutableListOf<EqualizerBand>()
        for (i in 0 until numBands) {
            val band = i.toShort()
            val centerFreq = eq.getCenterFreq(band) / 1000 // mHz to Hz
            val formatted = if (centerFreq >= 1000) "${centerFreq / 1000} kHz" else "$centerFreq Hz"
            val level = eq.getBandLevel(band)
            list.add(EqualizerBand(band, centerFreq, formatted, level))
        }
        return list
    }

    fun getBandLevelRange(): Pair<Short, Short> {
        val eq = equalizer ?: return Pair(-1500, 1500)
        val range = eq.bandLevelRange
        return Pair(range[0], range[1])
    }

    fun getBandLevel(band: Short): Short {
        return equalizer?.getBandLevel(band) ?: 0
    }

    fun setBandLevel(band: Short, levelMb: Short) {
        try {
            equalizer?.setBandLevel(band, levelMb)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getPresets(): List<String> {
        val eq = equalizer ?: return emptyList()
        val presets = mutableListOf<String>()
        for (i in 0 until eq.numberOfPresets) {
            presets.add(eq.getPresetName(i.toShort()))
        }
        return presets
    }

    fun getCurrentPreset(): Short {
        return equalizer?.currentPreset ?: -1
    }

    fun applyPreset(presetIndex: Short) {
        val eq = equalizer ?: return
        if (presetIndex in 0 until eq.numberOfPresets) {
            try {
                eq.usePreset(presetIndex)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setBassBoost(strength: Int) {
        bassBoost?.let {
            if (it.strengthSupported) {
                try {
                    it.setStrength(strength.coerceIn(0, 1000).toShort())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun getBassBoostStrength(): Int {
        return try {
            bassBoost?.roundedStrength?.toInt() ?: 0
        } catch (e: Exception) {
            0
        }
    }

    fun setVirtualizer(strength: Int) {
        virtualizer?.let {
            if (it.strengthSupported) {
                try {
                    it.setStrength(strength.coerceIn(0, 1000).toShort())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun getVirtualizerStrength(): Int {
        return try {
            virtualizer?.roundedStrength?.toInt() ?: 0
        } catch (e: Exception) {
            0
        }
    }

    fun release() {
        try {
            equalizer?.release()
            bassBoost?.release()
            virtualizer?.release()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            equalizer = null
            bassBoost = null
            virtualizer = null
        }
    }
}
