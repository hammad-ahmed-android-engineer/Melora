package com.example.melora.domain.usecase

import com.example.melora.domain.model.Album
import com.example.melora.domain.repository.MusicRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAlbumsUseCase @Inject constructor(
    private val musicRepository: MusicRepository
) {
    operator fun invoke(): Flow<List<Album>> = musicRepository.getAlbums()
}
