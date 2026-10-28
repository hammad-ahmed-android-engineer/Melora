package com.example.melora.data.local.db.entity

import androidx.room.Entity

@Entity(tableName = "playlist_songs", primaryKeys = ["playlistId", "songId"])
data class PlaylistSongEntity(
    val playlistId: Long,
    val songId: Long,
    val orderIndex: Int
)
