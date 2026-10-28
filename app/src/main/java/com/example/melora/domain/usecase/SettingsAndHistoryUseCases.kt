package com.example.melora.domain.usecase

import com.example.melora.domain.model.AppPreferences
import com.example.melora.domain.model.Song
import com.example.melora.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecentlyPlayedUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(): Flow<List<Song>> = settingsRepository.getRecentlyPlayed()
}

class GetMostPlayedUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(): Flow<List<Song>> = settingsRepository.getMostPlayed()
}

class UpdatePlaybackHistoryUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(songId: Long) {
        settingsRepository.recordPlayback(songId)
    }
}

class GetAppPreferencesUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(): Flow<AppPreferences> = settingsRepository.getAppPreferences()
}

class UpdateAppPreferencesUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(update: (AppPreferences) -> AppPreferences) {
        settingsRepository.updateAppPreferences(update)
    }
}

class ClearPlaybackHistoryUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke() {
        settingsRepository.clearHistory()
    }
}
