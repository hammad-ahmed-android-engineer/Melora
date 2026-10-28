package com.example.melora.domain.usecase

import com.example.melora.domain.model.Song
import com.example.melora.domain.repository.MusicRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchMusicUseCase @Inject constructor(
    private val musicRepository: MusicRepository
) {
    operator fun invoke(query: String): Flow<List<Song>> = musicRepository.searchMusic(query)
}
