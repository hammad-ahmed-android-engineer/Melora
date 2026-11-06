package com.example.melora.data.repository

import com.example.melora.data.local.db.dao.PlaylistDao
import com.example.melora.data.local.db.entity.PlaylistEntity
import com.example.melora.data.local.db.entity.PlaylistSongEntity
import com.example.melora.domain.model.Playlist
import com.example.melora.domain.model.Song
import com.example.melora.domain.repository.MusicRepository
import com.example.melora.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaylistRepositoryImpl @Inject constructor(
    private val playlistDao: PlaylistDao,
    private val musicRepository: MusicRepository
) : PlaylistRepository {

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return combine(
            playlistDao.getAllPlaylistsWithSongCount(),
            musicRepository.getAllSongs()
        ) { playlistsWithInfo, allSongs ->
            val songMap = allSongs.associateBy { it.id }
            playlistsWithInfo.map { item ->
                val artworkUri = item.firstSongId?.let { songMap[it]?.artworkUri }
                Playlist(
                    id = item.id,
                    name = item.name,
                    createdAt = item.createdAt,
                    songCount = item.songCount,
                    artworkUri = artworkUri
                )
            }
        }
    }

    override fun getPlaylistSongs(playlistId: Long): Flow<List<Song>> {
        return combine(playlistDao.getPlaylistSongIds(playlistId), musicRepository.getAllSongs()) { songIds, allSongs ->
            val songMap = allSongs.associateBy { it.id }
            songIds.mapNotNull { songMap[it] }
        }
    }

    override suspend fun createPlaylist(name: String): Long {
        return playlistDao.insertPlaylist(PlaylistEntity(name = name))
    }

    override suspend fun addSongToPlaylist(playlistId: Long, songId: Long) {
        val nextIndex = playlistDao.getNextOrderIndex(playlistId)
        playlistDao.insertPlaylistSong(
            PlaylistSongEntity(
                playlistId = playlistId,
                songId = songId,
                orderIndex = nextIndex
            )
        )
    }

    override suspend fun removeSongFromPlaylist(playlistId: Long, songId: Long) {
        playlistDao.removeSongFromPlaylist(playlistId, songId)
    }

    override suspend fun deletePlaylist(playlistId: Long) {
        playlistDao.clearPlaylistSongs(playlistId)
        playlistDao.deletePlaylist(playlistId)
    }

    override suspend fun reorderPlaylistSongs(playlistId: Long, songIds: List<Long>) {
        playlistDao.clearPlaylistSongs(playlistId)
        songIds.forEachIndexed { index, songId ->
            playlistDao.insertPlaylistSong(
                PlaylistSongEntity(
                    playlistId = playlistId,
                    songId = songId,
                    orderIndex = index
                )
            )
        }
    }
}
