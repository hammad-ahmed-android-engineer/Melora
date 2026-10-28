package com.example.melora.presentation.songs

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.melora.core.designsystem.components.MeloraCircularProgressIndicator
import com.example.melora.core.designsystem.components.SortDialog
import com.example.melora.core.designsystem.components.bottomsheets.SelectPlaylistBottomSheet
import com.example.melora.core.designsystem.components.bottomsheets.SongInfoBottomSheet
import com.example.melora.core.designsystem.components.dialogs.DeleteConfirmDialog
import com.example.melora.core.designsystem.components.dialogs.RenameSongDialog
import com.example.melora.core.utils.isLandscape
import com.example.melora.core.utils.shareAudioTrack
import com.example.melora.presentation.songs.components.EmptySongsView
import com.example.melora.presentation.songs.components.RecentlyPlayedTopSection
import com.example.melora.presentation.songs.components.SongFilterTabs
import com.example.melora.presentation.songs.components.SongsListContent
import com.example.melora.presentation.songs.components.SongsTopBar

/**
 * All songs screen composable featuring sorting, quick action bottom sheets/dialogs,
 * back handler navigation, loading indicator, and adaptive list/grid layout.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongsScreen(
    viewModel: SongsViewModel,
    onNavigateToPlayer: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showSortDialog by androidx.compose.runtime.saveable.rememberSaveable { mutableStateOf(false) }

    var selectedSongIdForPlaylist by androidx.compose.runtime.saveable.rememberSaveable { mutableStateOf<Long?>(null) }
    var selectedSongIdForInfo by androidx.compose.runtime.saveable.rememberSaveable { mutableStateOf<Long?>(null) }
    var selectedSongIdForRename by androidx.compose.runtime.saveable.rememberSaveable { mutableStateOf<Long?>(null) }
    var selectedSongIdForDelete by androidx.compose.runtime.saveable.rememberSaveable { mutableStateOf<Long?>(null) }

    val selectedSongForPlaylist = remember(selectedSongIdForPlaylist, uiState.filteredSongs, uiState.songs) {
        uiState.filteredSongs.find { it.id == selectedSongIdForPlaylist }
            ?: uiState.songs.find { it.id == selectedSongIdForPlaylist }
    }
    val selectedSongForInfo = remember(selectedSongIdForInfo, uiState.filteredSongs, uiState.songs) {
        uiState.filteredSongs.find { it.id == selectedSongIdForInfo }
            ?: uiState.songs.find { it.id == selectedSongIdForInfo }
    }
    val selectedSongForRename = remember(selectedSongIdForRename, uiState.filteredSongs, uiState.songs) {
        uiState.filteredSongs.find { it.id == selectedSongIdForRename }
            ?: uiState.songs.find { it.id == selectedSongIdForRename }
    }
    val selectedSongForDelete = remember(selectedSongIdForDelete, uiState.filteredSongs, uiState.songs) {
        uiState.filteredSongs.find { it.id == selectedSongIdForDelete }
            ?: uiState.songs.find { it.id == selectedSongIdForDelete }
    }

    val configuration = LocalConfiguration.current
    val landscape = configuration.isLandscape

    // Intercept back button gesture when in multi-selection or active search filter mode
    BackHandler(enabled = uiState.isMultiSelectMode || uiState.searchQuery.isNotEmpty()) {
        if (uiState.isMultiSelectMode) {
            viewModel.cancelSelection()
        } else {
            viewModel.updateSearchQuery("")
        }
    }

    var showMultiDeleteDialog by androidx.compose.runtime.saveable.rememberSaveable { mutableStateOf(false) }
    val context = androidx.compose.ui.platform.LocalContext.current

    Scaffold(
        topBar = {
            Column {
                SongsTopBar(
                    songsCount = uiState.filteredSongs.size,
                    isMultiSelectMode = uiState.isMultiSelectMode,
                    selectedCount = uiState.selectedSongIds.size,
                    isAllSelected = uiState.isAllSelected,
                    searchQuery = uiState.searchQuery,
                    onSearchQueryChange = { viewModel.updateSearchQuery(it) },
                    onRefresh = { viewModel.refreshLibrary() },
                    onOpenSort = { showSortDialog = true },
                    onShuffleAll = {
                        viewModel.shuffleAll()
                        onNavigateToPlayer()
                    },
                    onToggleSelectAll = { viewModel.toggleSelectAll() },
                    onCancelSelection = { viewModel.cancelSelection() },
                    onShareSelected = {
                        val selectedSongs = uiState.filteredSongs.filter { uiState.selectedSongIds.contains(it.id) }
                        if (selectedSongs.isNotEmpty()) {
                            context.shareAudioTrack(selectedSongs.first())
                        }
                    },
                    onDeleteSelected = {
                        if (uiState.selectedSongIds.isNotEmpty()) {
                            showMultiDeleteDialog = true
                        }
                    }
                )
                if (!uiState.isMultiSelectMode) {
                    SongFilterTabs(
                        selectedTab = uiState.selectedTab,
                        onTabSelected = { viewModel.selectTab(it) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    MeloraCircularProgressIndicator()
                }
            } else if (uiState.filteredSongs.isEmpty()) {
                EmptySongsView()
            } else {
                SongsListContent(
                    songs = uiState.filteredSongs,
                    isLandscape = landscape,
                    recentlyPlayedTopSongs = uiState.recentlyPlayedTopSongs,
                    onSongClick = { song ->
                        viewModel.playSong(song, uiState.filteredSongs)
                        onNavigateToPlayer()
                    },
                    onToggleFavorite = { viewModel.toggleFavorite(it) },
                    onAddToPlaylist = { selectedSongIdForPlaylist = it.id },
                    onPlayNext = { viewModel.playNext(it) },
                    onAddToQueue = { viewModel.addToQueue(it) },
                    onSongInfo = { selectedSongIdForInfo = it.id },
                    onRename = { selectedSongIdForRename = it.id },
                    onDelete = { selectedSongIdForDelete = it.id },
                    selectedSongIds = uiState.selectedSongIds,
                    isMultiSelectMode = uiState.isMultiSelectMode,
                    onToggleSelect = { viewModel.toggleSelectSong(it) },
                    onLongClickSong = { viewModel.toggleSelectSong(it.id) }
                )
            }
        }

        if (showSortDialog) {
            SortDialog(
                currentSortOption = uiState.sortOption,
                onSortSelected = { viewModel.updateSortOption(it) },
                onDismiss = { showSortDialog = false }
            )
        }

        selectedSongForPlaylist?.let { song ->
            SelectPlaylistBottomSheet(
                playlists = uiState.playlists,
                onPlaylistSelected = { playlistId ->
                    viewModel.addSongToPlaylist(playlistId, song.id)
                    selectedSongIdForPlaylist = null
                },
                onDismiss = { selectedSongIdForPlaylist = null }
            )
        }

        selectedSongForInfo?.let { song ->
            SongInfoBottomSheet(
                song = song,
                onDismiss = { selectedSongIdForInfo = null }
            )
        }

        selectedSongForRename?.let { song ->
            RenameSongDialog(
                song = song,
                onConfirm = { newTitle ->
                    viewModel.renameSong(song, newTitle)
                    selectedSongIdForRename = null
                },
                onDismiss = { selectedSongIdForRename = null }
            )
        }

        selectedSongForDelete?.let { song ->
            DeleteConfirmDialog(
                song = song,
                onConfirm = {
                    viewModel.deleteSong(song)
                    selectedSongIdForDelete = null
                },
                onDismiss = { selectedSongIdForDelete = null }
            )
        }

        if (showMultiDeleteDialog) {
            val selectedCount = uiState.selectedSongIds.size
            DeleteConfirmDialog(
                title = "Delete $selectedCount songs",
                message = "Are you sure you want to delete these $selectedCount songs?",
                onConfirm = {
                    val selectedSongs = uiState.filteredSongs.filter { uiState.selectedSongIds.contains(it.id) }
                    viewModel.deleteSelectedSongs(selectedSongs)
                    showMultiDeleteDialog = false
                },
                onDismiss = { showMultiDeleteDialog = false }
            )
        }
    }
}
