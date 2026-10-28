package com.example.melora.presentation.folders

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
import com.example.melora.presentation.folders.components.EmptyFoldersView
import com.example.melora.presentation.folders.components.FolderDetailContent
import com.example.melora.presentation.folders.components.FolderDetailTopBar
import com.example.melora.presentation.folders.components.FoldersListContent
import com.example.melora.presentation.folders.components.FoldersTopBar

/**
 * Folders screen composable displaying audio folders or folder contents
 * with BackHandler and landscape adaptive design using segregated components.
 */
@Composable
fun FoldersScreen(
    viewModel: FoldersViewModel,
    onNavigateToPlayer: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val folders = uiState.folders
    val selectedFolder = uiState.selectedFolder
    val folderSongs = uiState.selectedFolderSongs
    val isGridView = uiState.isGridView
    val configuration = LocalConfiguration.current
    val landscape = configuration.isLandscape

    BackHandler(enabled = selectedFolder != null) {
        viewModel.clearSelectedFolder()
    }

    if (selectedFolder == null) {
        Scaffold(
            topBar = {
                FoldersTopBar(
                    foldersCount = folders.size,
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
                if (folders.isEmpty()) {
                    EmptyFoldersView()
                } else {
                    FoldersListContent(
                        folders = folders,
                        isLandscape = landscape,
                        isGridView = isGridView,
                        onFolderClick = { viewModel.selectFolder(it) }
                    )
                }
            }
        }
    } else {
        selectedFolder.let { folder ->
            Scaffold(
                topBar = {
                    FolderDetailTopBar(
                        folderName = folder.name,
                        onBackClick = { viewModel.clearSelectedFolder() }
                    )
                }
            ) { innerPadding ->
                FolderDetailContent(
                    folder = folder,
                    songs = folderSongs,
                    onPlayAll = {
                        viewModel.playAll(folderSongs)
                        onNavigateToPlayer()
                    },
                    onSongClick = { song ->
                        viewModel.playSong(song, folderSongs)
                        onNavigateToPlayer()
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}
