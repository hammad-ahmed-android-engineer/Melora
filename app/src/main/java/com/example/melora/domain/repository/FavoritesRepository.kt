package com.example.melora.domain.repository

import com.example.melora.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun getFavoriteSongs(): Flow<List<Song>>
    fun isFavorite(songId: Long): Flow<Boolean>
    suspend fun toggleFavorite(songId: Long)
}
