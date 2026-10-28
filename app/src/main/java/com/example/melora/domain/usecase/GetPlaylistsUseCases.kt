package com.example.melora.domain.usecase

import com.example.melora.domain.model.Playlist
import com.example.melora.domain.model.Song
import com.example.melora.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllPlaylistsUseCase @Inject constructor(
    private val playlistRepository: PlaylistRepository
) {
    operator fun invoke(): Flow<List<Playlist>> = playlistRepository.getAllPlaylists()
}

class GetPlaylistSongsUseCase @Inject constructor(
    private val playlistRepository: PlaylistRepository
) {
    operator fun invoke(playlistId: Long): Flow<List<Song>> = playlistRepository.getPlaylistSongs(playlistId)
}
