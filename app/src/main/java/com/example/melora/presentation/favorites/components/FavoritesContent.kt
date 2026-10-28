package com.example.melora.presentation.favorites.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import com.example.melora.core.designsystem.components.MeloraButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.melora.R
import com.example.melora.core.utils.getGridColumnCount
import com.example.melora.domain.model.Song
import com.example.melora.core.designsystem.components.SongRowItem
import com.example.melora.core.designsystem.AppDimensions
import com.example.melora.core.designsystem.AppTypography

@Composable
fun FavoritesContent(
    favorites: List<Song>,
    isLandscape: Boolean,
    onPlayAll: () -> Unit,
    onSongClick: (Song) -> Unit,
    onToggleFavorite: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppDimensions.paddingMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MeloraButton(
                text = stringResource(R.string.play_favorites),
                onClick = onPlayAll
            )
        }
        if (isLandscape) {
            val columns = getGridColumnCount(true, portraitColumns = 1, landscapeColumns = 2)
            LazyVerticalGrid(
                columns = GridCells.Fixed(columns),
                contentPadding = PaddingValues(AppDimensions.paddingSmall),
                modifier = Modifier.fillMaxSize()
            ) {
                items(favorites, key = { it.id }) { song ->
                    SongRowItem(
                        song = song,
                        onClick = { onSongClick(song) },
                        onToggleFavorite = { onToggleFavorite(song.id) },
                        onAddToPlaylist = {}
                    )
                }
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(favorites, key = { it.id }) { song ->
                    SongRowItem(
                        song = song,
                        onClick = { onSongClick(song) },
                        onToggleFavorite = { onToggleFavorite(song.id) },
                        onAddToPlaylist = {}
                    )
                }
            }
        }
    }
}
