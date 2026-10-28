package com.example.melora.presentation.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.melora.core.utils.isLandscape
import com.example.melora.presentation.favorites.components.EmptyFavoritesView
import com.example.melora.presentation.favorites.components.FavoritesContent
import com.example.melora.presentation.favorites.components.FavoritesTopBar

/**
 * Favorites screen composable displaying user's favorite songs
 * with landscape adaptive design using segregated components.
 */
@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel,
    onNavigateToPlayer: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val favorites = uiState.favorites
    val configuration = LocalConfiguration.current
    val landscape = configuration.isLandscape

    Scaffold(
        topBar = { FavoritesTopBar(favoritesCount = favorites.size) }
    ) { innerPadding ->
        if (favorites.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                EmptyFavoritesView()
            }
        } else {
            FavoritesContent(
                favorites = favorites,
                isLandscape = landscape,
                onPlayAll = {
                    viewModel.playAll(favorites)
                    onNavigateToPlayer()
                },
                onSongClick = { song ->
                    viewModel.playSong(song, favorites)
                    onNavigateToPlayer()
                },
                onToggleFavorite = { viewModel.toggleFavorite(it) },
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
