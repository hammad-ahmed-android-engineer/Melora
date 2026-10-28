package com.example.melora.presentation.songs.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.melora.core.designsystem.AppDimensions
import com.example.melora.core.designsystem.components.SongRowItem
import com.example.melora.core.utils.getGridColumnCount
import com.example.melora.domain.model.Song

@Composable
fun SongsListContent(
    songs: List<Song>,
    isLandscape: Boolean,
    onSongClick: (Song) -> Unit,
    onToggleFavorite: (Long) -> Unit,
    onAddToPlaylist: (Song) -> Unit,
    onPlayNext: ((Song) -> Unit)? = null,
    onAddToQueue: ((Song) -> Unit)? = null,
    onSongInfo: (Song) -> Unit,
    onRename: (Song) -> Unit,
    onDelete: (Song) -> Unit,
    selectedSongIds: Set<Long> = emptySet(),
    isMultiSelectMode: Boolean = false,
    recentlyPlayedTopSongs: List<Song> = emptyList(),
    onToggleSelect: ((Long) -> Unit)? = null,
    onLongClickSong: ((Song) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    if (isLandscape) {
        val columns = getGridColumnCount(true, portraitColumns = 1, landscapeColumns = 2)
        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            contentPadding = PaddingValues(AppDimensions.paddingSmall),
            modifier = modifier.fillMaxSize()
        ) {
            if (recentlyPlayedTopSongs.isNotEmpty() && !isMultiSelectMode) {
                item(span = { GridItemSpan(columns) }) {
                    RecentlyPlayedTopSection(
                        songs = recentlyPlayedTopSongs,
                        onSongClick = onSongClick
                    )
                }
            }

            items(songs, key = { it.id }) { song ->
                SongRowItem(
                    song = song,
                    onClick = { onSongClick(song) },
                    onToggleFavorite = { onToggleFavorite(song.id) },
                    onAddToPlaylist = { onAddToPlaylist(song) },
                    onPlayNext = onPlayNext?.let { { it(song) } },
                    onAddToQueue = onAddToQueue?.let { { it(song) } },
                    onSongInfo = { onSongInfo(song) },
                    onRename = { onRename(song) },
                    onDelete = { onDelete(song) },
                    isSelected = selectedSongIds.contains(song.id),
                    isMultiSelectMode = isMultiSelectMode,
                    onToggleSelect = onToggleSelect?.let { { it(song.id) } },
                    onLongClick = onLongClickSong?.let { { it(song) } }
                )
            }
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(bottom = AppDimensions.paddingExtraLarge),
            modifier = modifier.fillMaxSize()
        ) {
            if (recentlyPlayedTopSongs.isNotEmpty() && !isMultiSelectMode) {
                item {
                    RecentlyPlayedTopSection(
                        songs = recentlyPlayedTopSongs,
                        onSongClick = onSongClick
                    )
                }
            }

            items(songs, key = { it.id }) { song ->
                SongRowItem(
                    song = song,
                    onClick = { onSongClick(song) },
                    onToggleFavorite = { onToggleFavorite(song.id) },
                    onAddToPlaylist = { onAddToPlaylist(song) },
                    onPlayNext = onPlayNext?.let { { it(song) } },
                    onAddToQueue = onAddToQueue?.let { { it(song) } },
                    onSongInfo = { onSongInfo(song) },
                    onRename = { onRename(song) },
                    onDelete = { onDelete(song) },
                    isSelected = selectedSongIds.contains(song.id),
                    isMultiSelectMode = isMultiSelectMode,
                    onToggleSelect = onToggleSelect?.let { { it(song.id) } },
                    onLongClick = onLongClickSong?.let { { it(song) } }
                )
            }
        }
    }
}
