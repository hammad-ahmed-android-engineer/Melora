package com.example.melora.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.melora.data.local.db.entity.HistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Query("SELECT songId FROM playback_history ORDER BY lastPlayedTimestamp DESC LIMIT 50")
    fun getRecentlyPlayedSongIds(): Flow<List<Long>>

    @Query("SELECT songId FROM playback_history ORDER BY playCount DESC LIMIT 50")
    fun getMostPlayedSongIds(): Flow<List<Long>>

    @Query("SELECT * FROM playback_history")
    fun getAllHistory(): Flow<List<HistoryEntity>>

    @Query("SELECT * FROM playback_history WHERE songId = :songId")
    suspend fun getHistoryItem(songId: Long): HistoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(history: HistoryEntity)

    @Query("DELETE FROM playback_history")
    suspend fun clearHistory(): Int
}
