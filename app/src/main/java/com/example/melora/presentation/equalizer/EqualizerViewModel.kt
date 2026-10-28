package com.example.melora.presentation.equalizer

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melora.domain.model.EqualizerBand
import com.example.melora.domain.model.EqualizerState
import com.example.melora.domain.usecase.ApplyEqualizerPresetUseCase
import com.example.melora.domain.usecase.GetEqualizerStateUseCase
import com.example.melora.domain.usecase.RefreshEqualizerStateUseCase
import com.example.melora.domain.usecase.SetBassBoostUseCase
import com.example.melora.domain.usecase.SetEqualizerBandLevelUseCase
import com.example.melora.domain.usecase.SetEqualizerEnabledUseCase
import com.example.melora.domain.usecase.SetVirtualizerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@Immutable
data class EqualizerUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
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

@HiltViewModel
class EqualizerViewModel @Inject constructor(
    getEqualizerStateUseCase: GetEqualizerStateUseCase,
    private val setEqualizerEnabledUseCase: SetEqualizerEnabledUseCase,
    private val setEqualizerBandLevelUseCase: SetEqualizerBandLevelUseCase,
    private val applyEqualizerPresetUseCase: ApplyEqualizerPresetUseCase,
    private val setBassBoostUseCase: SetBassBoostUseCase,
    private val setVirtualizerUseCase: SetVirtualizerUseCase,
    private val refreshEqualizerStateUseCase: RefreshEqualizerStateUseCase
) : ViewModel() {

    private val _errorMessage = MutableStateFlow<String?>(null)

    val uiState: StateFlow<EqualizerUiState> = combine(
        getEqualizerStateUseCase(),
        _errorMessage
    ) { state, error ->
        EqualizerUiState(
            isAvailable = state.isAvailable,
            isEnabled = state.isEnabled,
            bands = state.bands,
            minLevelMb = state.minLevelMb,
            maxLevelMb = state.maxLevelMb,
            presets = state.presets,
            currentPreset = state.currentPreset,
            bassBoostStrength = state.bassBoostStrength,
            virtualizerStrength = state.virtualizerStrength,
            errorMessage = error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = EqualizerUiState()
    )

    fun clearError() {
        _errorMessage.value = null
    }

    fun toggleEnabled(enabled: Boolean) {
        setEqualizerEnabledUseCase(enabled)
    }

    fun setBandLevel(band: Short, levelMb: Short) {
        setEqualizerBandLevelUseCase(band, levelMb)
    }

    fun applyPreset(presetIndex: Short) {
        applyEqualizerPresetUseCase(presetIndex)
    }

    fun setBassBoost(strength: Int) {
        setBassBoostUseCase(strength)
    }

    fun setVirtualizer(strength: Int) {
        setVirtualizerUseCase(strength)
    }

    fun refreshState() {
        refreshEqualizerStateUseCase()
    }
}
