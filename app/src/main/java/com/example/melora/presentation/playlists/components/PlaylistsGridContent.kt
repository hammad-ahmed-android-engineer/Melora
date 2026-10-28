package com.example.melora.presentation.playlists.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.melora.core.utils.getGridColumnCount
import com.example.melora.domain.model.Playlist
import com.example.melora.core.designsystem.AppDimensions

import com.example.melora.core.designsystem.components.PlaylistCardItem

@Composable
fun PlaylistsGridContent(
    playlists: List<Playlist>,
    isLandscape: Boolean,
    isGridView: Boolean = false,
    onPlaylistClick: (Playlist) -> Unit,
    onPlayPlaylist: (Playlist) -> Unit = {},
    onShufflePlaylist: (Playlist) -> Unit = {},
    onDuplicatePlaylist: (Playlist) -> Unit = {},
    onDeletePlaylist: (Playlist) -> Unit = {},
    modifier: Modifier = Modifier
) {
    if (isGridView) {
        val columns = getGridColumnCount(isLandscape, portraitColumns = 2, landscapeColumns = 4)
        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            contentPadding = PaddingValues(AppDimensions.paddingMedium),
            horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall),
            verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall),
            modifier = modifier.fillMaxSize()
        ) {
            items(playlists, key = { it.id }) { playlist ->
                PlaylistCardItem(
                    playlist = playlist,
                    onClick = { onPlaylistClick(playlist) }
                )
            }
        }
    } else {
        if (isLandscape) {
            val columns = getGridColumnCount(isLandscape, portraitColumns = 1, landscapeColumns = 2)
            LazyVerticalGrid(
                columns = GridCells.Fixed(columns),
                contentPadding = PaddingValues(bottom = AppDimensions.paddingExtraLarge),
                horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall),
                verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall),
                modifier = modifier.fillMaxSize()
            ) {
                items(playlists, key = { it.id }) { playlist ->
                    PlaylistListItem(
                        playlist = playlist,
                        onClick = { onPlaylistClick(playlist) },
                        onPlay = { onPlayPlaylist(playlist) },
                        onShuffle = { onShufflePlaylist(playlist) },
                        onDuplicate = { onDuplicatePlaylist(playlist) },
                        onDelete = { onDeletePlaylist(playlist) }
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(bottom = AppDimensions.paddingExtraLarge),
                modifier = modifier.fillMaxSize()
            ) {
                items(playlists, key = { it.id }) { playlist ->
                    PlaylistListItem(
                        playlist = playlist,
                        onClick = { onPlaylistClick(playlist) },
                        onPlay = { onPlayPlaylist(playlist) },
                        onShuffle = { onShufflePlaylist(playlist) },
                        onDuplicate = { onDuplicatePlaylist(playlist) },
                        onDelete = { onDeletePlaylist(playlist) }
                    )
                }
            }
        }
    }
}

