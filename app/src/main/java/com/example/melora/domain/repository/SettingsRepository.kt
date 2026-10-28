package com.example.melora.domain.repository

import com.example.melora.domain.model.AppPreferences
import com.example.melora.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getAppPreferences(): Flow<AppPreferences>
    suspend fun updateAppPreferences(update: (AppPreferences) -> AppPreferences)
    fun getRecentlyPlayed(): Flow<List<Song>>
    fun getMostPlayed(): Flow<List<Song>>
    suspend fun recordPlayback(songId: Long)
    suspend fun clearHistory()
}
