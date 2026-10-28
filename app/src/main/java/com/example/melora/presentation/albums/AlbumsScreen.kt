package com.example.melora.presentation.albums

import androidx.activity.compose.BackHandler
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
import com.example.melora.presentation.albums.components.AlbumDetailContent
import com.example.melora.presentation.albums.components.AlbumDetailTopBar
import com.example.melora.presentation.albums.components.AlbumsGridContent
import com.example.melora.presentation.albums.components.AlbumsTopBar
import com.example.melora.presentation.albums.components.EmptyAlbumsView

/**
 * Albums screen composable displaying grid of albums or album song list detail
 * with BackHandler and landscape adaptive design using segregated components.
 */
@Composable
fun AlbumsScreen(
    viewModel: AlbumsViewModel,
    onNavigateToPlayer: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val albums = uiState.albums
    val selectedAlbum = uiState.selectedAlbum
    val albumSongs = uiState.selectedAlbumSongs
    val searchQuery = uiState.searchQuery
    val isGridView = uiState.isGridView

    val configuration = LocalConfiguration.current
    val landscape = configuration.isLandscape

    BackHandler(enabled = selectedAlbum != null) {
        viewModel.clearSelectedAlbum()
    }

    if (selectedAlbum == null) {
        Scaffold(
            topBar = {
                AlbumsTopBar(
                    albumsCount = albums.size,
                    searchQuery = searchQuery,
                    onSearchQueryChange = { viewModel.updateSearchQuery(it) },
                    isGridView = isGridView,
                    onToggleView = { viewModel.toggleViewMode() }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (albums.isEmpty()) {
                    EmptyAlbumsView()
                } else {
                    AlbumsGridContent(
                        albums = albums,
                        isLandscape = landscape,
                        isGridView = isGridView,
                        onAlbumClick = { viewModel.selectAlbum(it) }
                    )
                }
            }
        }
    } else {
        selectedAlbum.let { album ->
            Scaffold(
                topBar = {
                    AlbumDetailTopBar(
                        albumTitle = album.title,
                        onBackClick = { viewModel.clearSelectedAlbum() }
                    )
                }
            ) { innerPadding ->
                AlbumDetailContent(
                    album = album,
                    songs = albumSongs,
                    onPlayAll = {
                        viewModel.playAll(albumSongs)
                        onNavigateToPlayer()
                    },
                    onShuffleAll = {
                        viewModel.shuffleAll(albumSongs)
                        onNavigateToPlayer()
                    },
                    onSongClick = { song ->
                        viewModel.playSong(song, albumSongs)
                        onNavigateToPlayer()
                    },
                    onAddToQueue = { viewModel.addToQueue(it) },
                    onAddAlbumToQueue = { viewModel.addAlbumToQueue(albumSongs) },
                    onToggleFavorite = { viewModel.toggleFavorite(it) },
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}
