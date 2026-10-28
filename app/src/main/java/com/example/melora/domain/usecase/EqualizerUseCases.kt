package com.example.melora.domain.usecase

import com.example.melora.domain.model.EqualizerState
import com.example.melora.domain.repository.EqualizerRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetEqualizerStateUseCase @Inject constructor(
    private val equalizerRepository: EqualizerRepository
) {
    operator fun invoke(): StateFlow<EqualizerState> = equalizerRepository.equalizerState
}

class SetEqualizerEnabledUseCase @Inject constructor(
    private val equalizerRepository: EqualizerRepository
) {
    operator fun invoke(enabled: Boolean) = equalizerRepository.setEnabled(enabled)
}

class SetEqualizerBandLevelUseCase @Inject constructor(
    private val equalizerRepository: EqualizerRepository
) {
    operator fun invoke(band: Short, levelMb: Short) = equalizerRepository.setBandLevel(band, levelMb)
}

class ApplyEqualizerPresetUseCase @Inject constructor(
    private val equalizerRepository: EqualizerRepository
) {
    operator fun invoke(presetIndex: Short) = equalizerRepository.applyPreset(presetIndex)
}

class SetBassBoostUseCase @Inject constructor(
    private val equalizerRepository: EqualizerRepository
) {
    operator fun invoke(strength: Int) = equalizerRepository.setBassBoost(strength)
}

class SetVirtualizerUseCase @Inject constructor(
    private val equalizerRepository: EqualizerRepository
) {
    operator fun invoke(strength: Int) = equalizerRepository.setVirtualizer(strength)
}

class RefreshEqualizerStateUseCase @Inject constructor(
    private val equalizerRepository: EqualizerRepository
) {
    operator fun invoke() = equalizerRepository.refreshState()
}
