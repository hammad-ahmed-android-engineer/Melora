package com.example.melora.domain.usecase

import com.example.melora.domain.model.Song
import com.example.melora.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteSongsUseCase @Inject constructor(
    private val favoritesRepository: FavoritesRepository
) {
    operator fun invoke(): Flow<List<Song>> = favoritesRepository.getFavoriteSongs()
}
