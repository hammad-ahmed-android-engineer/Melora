package com.example.melora.presentation.artists

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
import com.example.melora.presentation.artists.components.ArtistDetailContent
import com.example.melora.presentation.artists.components.ArtistDetailTopBar
import com.example.melora.presentation.artists.components.ArtistsListContent
import com.example.melora.presentation.artists.components.ArtistsTopBar
import com.example.melora.presentation.artists.components.EmptyArtistsView

/**
 * Artists screen composable displaying artist list or artist songs detail
 * with BackHandler and landscape adaptive design using segregated components.
 */
@Composable
fun ArtistsScreen(
    viewModel: ArtistsViewModel,
    onNavigateToPlayer: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val artists = uiState.artists
    val selectedArtist = uiState.selectedArtist
    val artistSongs = uiState.selectedArtistSongs
    val isGridView = uiState.isGridView
    val configuration = LocalConfiguration.current
    val landscape = configuration.isLandscape

    BackHandler(enabled = selectedArtist != null) {
        viewModel.clearSelectedArtist()
    }

    if (selectedArtist == null) {
        Scaffold(
            topBar = { 
                ArtistsTopBar(
                    artistsCount = artists.size,
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
                if (artists.isEmpty()) {
                    EmptyArtistsView()
                } else {
                    ArtistsListContent(
                        artists = artists,
                        isLandscape = landscape,
                        isGridView = isGridView,
                        onArtistClick = { viewModel.selectArtist(it) }
                    )
                }
            }
        }
    } else {
        selectedArtist.let { artist ->
            Scaffold(
                topBar = {
                    ArtistDetailTopBar(
                        artistName = artist.name,
                        onBackClick = { viewModel.clearSelectedArtist() }
                    )
                }
            ) { innerPadding ->
                ArtistDetailContent(
                    artist = artist,
                    songs = artistSongs,
                    onPlayAll = {
                        viewModel.playAll(artistSongs)
                        onNavigateToPlayer()
                    },
                    onSongClick = { song ->
                        viewModel.playSong(song, artistSongs)
                        onNavigateToPlayer()
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}
