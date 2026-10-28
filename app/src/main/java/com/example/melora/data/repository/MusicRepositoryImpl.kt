package com.example.melora.data.repository

import com.example.melora.data.local.db.dao.FavoriteDao
import com.example.melora.data.local.mediastore.MediaStoreAudioSource
import com.example.melora.domain.model.Album
import com.example.melora.domain.model.Artist
import com.example.melora.domain.model.AudioFolder
import com.example.melora.domain.model.Song
import com.example.melora.domain.repository.MusicRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicRepositoryImpl @Inject constructor(
    private val mediaStoreAudioSource: MediaStoreAudioSource,
    private val favoriteDao: FavoriteDao,
    private val historyDao: com.example.melora.data.local.db.dao.HistoryDao
) : MusicRepository {

    private val rawSongsFlow = MutableStateFlow<List<Song>>(emptyList())

    override suspend fun refreshLibrary() {
        val songs = mediaStoreAudioSource.getSongs()
        rawSongsFlow.value = songs
    }

    override fun getAllSongs(): Flow<List<Song>> {
        return combine(
            rawSongsFlow,
            favoriteDao.getFavoriteSongIds(),
            historyDao.getAllHistory()
        ) { songs, favoriteIds, historyList ->
            val favSet = favoriteIds.toSet()
            val historyMap = historyList.associateBy { it.songId }
            songs.map { song ->
                val history = historyMap[song.id]
                song.copy(
                    isFavorite = favSet.contains(song.id),
                    lastPlayed = history?.lastPlayedTimestamp ?: 0L,
                    playCount = history?.playCount ?: 0
                )
            }
        }
    }

    override fun getAlbums(): Flow<List<Album>> {
        return getAllSongs().map { songs ->
            songs.groupBy { it.albumId }.map { (albumId, albumSongs) ->
                val first = albumSongs.first()
                Album(
                    id = albumId,
                    title = first.album,
                    artist = first.artist,
                    songCount = albumSongs.size,
                    artworkUri = first.artworkUri,
                    year = albumSongs.maxOfOrNull { it.year } ?: 0
                )
            }.sortedBy { it.title.lowercase() }
        }
    }

    override fun getArtists(): Flow<List<Artist>> {
        return getAllSongs().map { songs ->
            songs.groupBy { it.artistId }.map { (artistId, artistSongs) ->
                val first = artistSongs.first()
                val albumCount = artistSongs.map { it.albumId }.distinct().size
                Artist(
                    id = artistId,
                    name = first.artist,
                    songCount = artistSongs.size,
                    albumCount = albumCount,
                    artworkUri = artistSongs.firstOrNull { it.artworkUri != null }?.artworkUri
                )
            }.sortedBy { it.name.lowercase() }
        }
    }

    override fun getFolders(): Flow<List<AudioFolder>> {
        return getAllSongs().map { songs ->
            val folderMap = mutableMapOf<String, MutableList<Song>>()
            songs.forEach { song ->
                val path = song.filePath
                if (path != null) {
                    val folderPath = File(path).parent ?: "Unknown"
                    folderMap.getOrPut(folderPath) { mutableListOf() }.add(song)
                }
            }
            folderMap.map { (path, folderSongs) ->
                AudioFolder(
                    path = path,
                    name = File(path).name.ifBlank { "Root" },
                    songCount = folderSongs.size
                )
            }.sortedBy { it.name.lowercase() }
        }
    }

    override fun searchMusic(query: String): Flow<List<Song>> {
        val q = query.trim().lowercase()
        return getAllSongs().map { songs ->
            if (q.isEmpty()) emptyList()
            else songs.filter { song ->
                song.title.lowercase().contains(q) ||
                        song.artist.lowercase().contains(q) ||
                        song.album.lowercase().contains(q)
            }
        }
    }

    override suspend fun getSongById(id: Long): Song? {
        val songs = mediaStoreAudioSource.getSongs()
        return songs.find { it.id == id }
    }

    override fun getSongsByAlbum(albumId: Long): Flow<List<Song>> {
        return getAllSongs().map { songs ->
            songs.filter { it.albumId == albumId }
        }
    }

    override fun getSongsByArtist(artistId: Long): Flow<List<Song>> {
        return getAllSongs().map { songs ->
            songs.filter { it.artistId == artistId }
        }
    }

    override fun getSongsByFolder(folderPath: String): Flow<List<Song>> {
        return getAllSongs().map { songs ->
            songs.filter { song ->
                song.filePath != null && File(song.filePath).parent == folderPath
            }
        }
    }

    override suspend fun deleteSong(song: Song): Boolean {
        return deleteSongs(listOf(song))
    }

    override suspend fun deleteSongs(songs: List<Song>): Boolean {
        if (songs.isEmpty()) return false
        var anySuccess = false
        songs.forEach { song ->
            if (mediaStoreAudioSource.deleteSong(song)) {
                anySuccess = true
            }
        }
        if (anySuccess) {
            refreshLibrary()
        }
        return anySuccess
    }

    override suspend fun renameSong(song: Song, newTitle: String): Boolean {
        val success = mediaStoreAudioSource.renameSong(song, newTitle)
        if (success) {
            refreshLibrary()
        }
        return success
    }
}
