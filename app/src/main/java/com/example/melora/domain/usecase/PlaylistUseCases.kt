package com.example.melora.domain.usecase

import com.example.melora.domain.repository.PlaylistRepository
import javax.inject.Inject

class CreatePlaylistUseCase @Inject constructor(
    private val playlistRepository: PlaylistRepository
) {
    suspend operator fun invoke(name: String): Long = playlistRepository.createPlaylist(name)
}

class AddSongToPlaylistUseCase @Inject constructor(
    private val playlistRepository: PlaylistRepository
) {
    suspend operator fun invoke(playlistId: Long, songId: Long) {
        playlistRepository.addSongToPlaylist(playlistId, songId)
    }
}

class RemoveSongFromPlaylistUseCase @Inject constructor(
    private val playlistRepository: PlaylistRepository
) {
    suspend operator fun invoke(playlistId: Long, songId: Long) {
        playlistRepository.removeSongFromPlaylist(playlistId, songId)
    }
}

class DeletePlaylistUseCase @Inject constructor(
    private val playlistRepository: PlaylistRepository
) {
    suspend operator fun invoke(playlistId: Long) {
        playlistRepository.deletePlaylist(playlistId)
    }
}
