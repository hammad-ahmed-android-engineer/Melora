package com.example.melora.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playback_history")
data class HistoryEntity(
    @PrimaryKey val songId: Long,
    val lastPlayedTimestamp: Long = System.currentTimeMillis(),
    val playCount: Int = 1
)
