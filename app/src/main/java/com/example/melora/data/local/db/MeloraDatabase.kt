package com.example.melora.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.melora.data.local.db.dao.FavoriteDao
import com.example.melora.data.local.db.dao.HistoryDao
import com.example.melora.data.local.db.dao.PlaylistDao
import com.example.melora.data.local.db.entity.FavoriteEntity
import com.example.melora.data.local.db.entity.HistoryEntity
import com.example.melora.data.local.db.entity.PlaylistEntity
import com.example.melora.data.local.db.entity.PlaylistSongEntity

@Database(
    entities = [
        PlaylistEntity::class,
        PlaylistSongEntity::class,
        FavoriteEntity::class,
        HistoryEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MeloraDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun historyDao(): HistoryDao
}
