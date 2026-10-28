package com.example.melora.domain.repository

import com.example.melora.domain.model.Playlist
import com.example.melora.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    fun getAllPlaylists(): Flow<List<Playlist>>
    fun getPlaylistSongs(playlistId: Long): Flow<List<Song>>
    suspend fun createPlaylist(name: String): Long
    suspend fun addSongToPlaylist(playlistId: Long, songId: Long)
    suspend fun removeSongFromPlaylist(playlistId: Long, songId: Long)
    suspend fun deletePlaylist(playlistId: Long)
    suspend fun reorderPlaylistSongs(playlistId: Long, songIds: List<Long>)
}
