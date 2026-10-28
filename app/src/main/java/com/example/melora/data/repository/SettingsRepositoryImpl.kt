package com.example.melora.data.repository

import com.example.melora.data.datastore.PreferencesDataSource
import com.example.melora.data.local.db.dao.HistoryDao
import com.example.melora.data.local.db.entity.HistoryEntity
import com.example.melora.domain.model.AppPreferences
import com.example.melora.domain.model.Song
import com.example.melora.domain.repository.MusicRepository
import com.example.melora.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val preferencesDataSource: PreferencesDataSource,
    private val historyDao: HistoryDao,
    private val musicRepository: MusicRepository
) : SettingsRepository {

    override fun getAppPreferences(): Flow<AppPreferences> {
        return preferencesDataSource.appPreferencesFlow
    }

    override suspend fun updateAppPreferences(update: (AppPreferences) -> AppPreferences) {
        preferencesDataSource.updatePreferences(update)
    }

    override fun getRecentlyPlayed(): Flow<List<Song>> {
        return combine(historyDao.getRecentlyPlayedSongIds(), musicRepository.getAllSongs()) { historyIds, allSongs ->
            val songMap = allSongs.associateBy { it.id }
            historyIds.mapNotNull { songMap[it] }
        }
    }

    override fun getMostPlayed(): Flow<List<Song>> {
        return combine(historyDao.getMostPlayedSongIds(), musicRepository.getAllSongs()) { historyIds, allSongs ->
            val songMap = allSongs.associateBy { it.id }
            historyIds.mapNotNull { songMap[it] }
        }
    }

    override suspend fun recordPlayback(songId: Long) {
        val existing = historyDao.getHistoryItem(songId)
        if (existing != null) {
            historyDao.insertOrUpdate(
                existing.copy(
                    lastPlayedTimestamp = System.currentTimeMillis(),
                    playCount = existing.playCount + 1
                )
            )
        } else {
            historyDao.insertOrUpdate(
                HistoryEntity(
                    songId = songId,
                    lastPlayedTimestamp = System.currentTimeMillis(),
                    playCount = 1
                )
            )
        }
    }

    override suspend fun clearHistory() {
        historyDao.clearHistory()
    }
}
