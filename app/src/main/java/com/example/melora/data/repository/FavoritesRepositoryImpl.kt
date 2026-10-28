package com.example.melora.data.repository

import com.example.melora.data.local.db.dao.FavoriteDao
import com.example.melora.data.local.db.entity.FavoriteEntity
import com.example.melora.domain.model.Song
import com.example.melora.domain.repository.FavoritesRepository
import com.example.melora.domain.repository.MusicRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepositoryImpl @Inject constructor(
    private val favoriteDao: FavoriteDao,
    private val musicRepository: MusicRepository
) : FavoritesRepository {

    override fun getFavoriteSongs(): Flow<List<Song>> {
        return combine(favoriteDao.getFavoriteSongIds(), musicRepository.getAllSongs()) { favIds, allSongs ->
            val favSet = favIds.toSet()
            allSongs.filter { favSet.contains(it.id) }.map { it.copy(isFavorite = true) }
        }
    }

    override fun isFavorite(songId: Long): Flow<Boolean> {
        return favoriteDao.isFavorite(songId)
    }

    override suspend fun toggleFavorite(songId: Long) {
        val currentlyFav = favoriteDao.isFavorite(songId).first()
        if (currentlyFav) {
            favoriteDao.removeFavorite(songId)
        } else {
            favoriteDao.addFavorite(FavoriteEntity(songId = songId))
        }
    }
}
