package com.example.melora.domain.repository

import com.example.melora.domain.model.Album
import com.example.melora.domain.model.Artist
import com.example.melora.domain.model.AudioFolder
import com.example.melora.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface MusicRepository {
    fun getAllSongs(): Flow<List<Song>>
    fun getAlbums(): Flow<List<Album>>
    fun getArtists(): Flow<List<Artist>>
    fun getFolders(): Flow<List<AudioFolder>>
    fun searchMusic(query: String): Flow<List<Song>>
    suspend fun getSongById(id: Long): Song?
    fun getSongsByAlbum(albumId: Long): Flow<List<Song>>
    fun getSongsByArtist(artistId: Long): Flow<List<Song>>
    fun getSongsByFolder(folderPath: String): Flow<List<Song>>
    suspend fun refreshLibrary()
    suspend fun deleteSong(song: Song): Boolean
    suspend fun deleteSongs(songs: List<Song>): Boolean
    suspend fun renameSong(song: Song, newTitle: String): Boolean
}
