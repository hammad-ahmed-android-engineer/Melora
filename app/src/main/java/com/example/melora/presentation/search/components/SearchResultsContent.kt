package com.example.melora.presentation.search.components

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
import com.example.melora.domain.model.Song
import com.example.melora.core.designsystem.components.SongRowItem
import com.example.melora.core.designsystem.AppDimensions

@Composable
fun SearchResultsContent(
    results: List<Song>,
    isLandscape: Boolean,
    onSongClick: (Song) -> Unit,
    modifier: Modifier = Modifier
) {
    if (isLandscape) {
        val columns = getGridColumnCount(true, portraitColumns = 1, landscapeColumns = 2)
        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            contentPadding = PaddingValues(AppDimensions.paddingSmall),
            modifier = modifier.fillMaxSize()
        ) {
            items(results, key = { it.id }) { song ->
                SongRowItem(
                    song = song,
                    onClick = { onSongClick(song) },
                    onToggleFavorite = {},
                    onAddToPlaylist = {}
                )
            }
        }
    } else {
        LazyColumn(modifier = modifier.fillMaxSize()) {
            items(results, key = { it.id }) { song ->
                SongRowItem(
                    song = song,
                    onClick = { onSongClick(song) },
                    onToggleFavorite = {},
                    onAddToPlaylist = {}
                )
            }
        }
    }
}
