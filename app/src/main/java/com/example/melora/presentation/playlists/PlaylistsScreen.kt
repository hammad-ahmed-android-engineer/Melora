package com.example.melora.presentation.playlists

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.melora.R
import com.example.melora.core.utils.isLandscape
import com.example.melora.core.designsystem.components.dialogs.DeleteConfirmDialog
import com.example.melora.presentation.playlists.components.AddSongsToPlaylistDialog
import com.example.melora.presentation.playlists.components.CreatePlaylistDialog
import com.example.melora.presentation.playlists.components.EmptyPlaylistsView
import com.example.melora.presentation.playlists.components.PlaylistDetailContent
import com.example.melora.presentation.playlists.components.PlaylistDetailTopBar
import com.example.melora.presentation.playlists.components.PlaylistsGridContent
import com.example.melora.presentation.playlists.components.PlaylistsTopBar

/**
 * Playlists screen composable displaying grid of playlists or playlist songs detail.
 */
@Composable
fun PlaylistsScreen(
    viewModel: PlaylistsViewModel,
    onNavigateToPlayer: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val playlists = uiState.playlists
    val allLibrarySongs = uiState.allLibrarySongs
    val selectedPlaylist = uiState.selectedPlaylist
    val playlistSongs = uiState.selectedPlaylistSongs

    var showCreateDialog by androidx.compose.runtime.saveable.rememberSaveable { mutableStateOf(false) }
    var showDeletePlaylistDialog by androidx.compose.runtime.saveable.rememberSaveable { mutableStateOf(false) }
    var showAddSongsDialog by androidx.compose.runtime.saveable.rememberSaveable { mutableStateOf(false) }

    val isGridView = uiState.isGridView

    val configuration = LocalConfiguration.current
    val landscape = configuration.isLandscape

    BackHandler(enabled = selectedPlaylist != null) {
        viewModel.clearSelectedPlaylist()
    }

    if (selectedPlaylist == null) {
        Scaffold(
            topBar = {
                PlaylistsTopBar(
                    playlistsCount = playlists.size,
                    isGridView = isGridView,
                    onToggleView = { viewModel.toggleViewMode() },
                    onCreatePlaylist = { showCreateDialog = true }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (playlists.isEmpty()) {
                    EmptyPlaylistsView()
                } else {
                    PlaylistsGridContent(
                        playlists = playlists,
                        isLandscape = landscape,
                        isGridView = isGridView,
                        onPlaylistClick = { viewModel.selectPlaylist(it) },
                        onDuplicatePlaylist = { viewModel.duplicatePlaylist(it) },
                        onDeletePlaylist = { viewModel.deletePlaylist(it.id) }
                    )
                }
            }
        }
    } else {
        selectedPlaylist.let { playlist ->
            Scaffold(
                topBar = {
                    PlaylistDetailTopBar(
                        playlistName = playlist.name,
                        onBackClick = { viewModel.clearSelectedPlaylist() },
                        onDeleteClick = { showDeletePlaylistDialog = true }
                    )
                }
            ) { innerPadding ->
                PlaylistDetailContent(
                    playlist = playlist,
                    songs = playlistSongs,
                    onPlayAll = {
                        viewModel.playAll(playlistSongs)
                        onNavigateToPlayer()
                    },
                    onAddSongs = { showAddSongsDialog = true },
                    onSongClick = { song ->
                        viewModel.playSong(song, playlistSongs)
                        onNavigateToPlayer()
                    },
                    onRemoveSong = { song ->
                        viewModel.removeSongFromPlaylist(playlist.id, song.id)
                    },
                    modifier = Modifier.padding(innerPadding)
                )

                if (showDeletePlaylistDialog) {
                    DeleteConfirmDialog(
                        title = stringResource(R.string.delete_playlist),
                        message = stringResource(R.string.delete_playlist_confirm, playlist.name),
                        onConfirm = {
                            viewModel.deletePlaylist(playlist.id)
                            viewModel.clearSelectedPlaylist()
                            showDeletePlaylistDialog = false
                        },
                        onDismiss = { showDeletePlaylistDialog = false }
                    )
                }

                if (showAddSongsDialog) {
                    AddSongsToPlaylistDialog(
                        availableSongs = allLibrarySongs,
                        existingSongIds = playlistSongs.map { it.id }.toSet(),
                        onSongSelected = { song ->
                            viewModel.addSongToPlaylist(playlist.id, song.id)
                        },
                        onDismiss = { showAddSongsDialog = false }
                    )
                }
            }
        }
    }

    if (showCreateDialog) {
        CreatePlaylistDialog(
            onConfirm = { name ->
                viewModel.createPlaylist(name)
                showCreateDialog = false
            },
            onDismiss = { showCreateDialog = false }
        )
    }
}
