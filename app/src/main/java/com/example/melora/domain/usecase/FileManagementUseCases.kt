package com.example.melora.domain.usecase

import com.example.melora.domain.model.Song
import com.example.melora.domain.repository.MusicRepository
import javax.inject.Inject

class DeleteSongUseCase @Inject constructor(
    private val musicRepository: MusicRepository
) {
    suspend operator fun invoke(song: Song): Boolean = musicRepository.deleteSong(song)
    suspend operator fun invoke(songs: List<Song>): Boolean = musicRepository.deleteSongs(songs)
}

class RenameSongUseCase @Inject constructor(
    private val musicRepository: MusicRepository
) {
    suspend operator fun invoke(song: Song, newTitle: String): Boolean = musicRepository.renameSong(song, newTitle)
}
