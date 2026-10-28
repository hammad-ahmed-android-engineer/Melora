package com.example.melora.domain.usecase

import com.example.melora.domain.repository.FavoritesRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val favoritesRepository: FavoritesRepository
) {
    suspend operator fun invoke(songId: Long) {
        favoritesRepository.toggleFavorite(songId)
    }
}
