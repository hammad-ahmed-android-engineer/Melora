package com.example.melora.presentation.artists.components

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
import com.example.melora.core.designsystem.AppDimensions
import com.example.melora.core.designsystem.components.ArtistCardItem
import com.example.melora.core.designsystem.components.ArtistRowItem
import com.example.melora.core.utils.getGridColumnCount
import com.example.melora.domain.model.Artist

@Composable
fun ArtistsListContent(
    artists: List<Artist>,
    isLandscape: Boolean,
    isGridView: Boolean = false,
    onArtistClick: (Artist) -> Unit,
    modifier: Modifier = Modifier
) {
    if (isGridView) {
        val columns = getGridColumnCount(isLandscape, portraitColumns = 3, landscapeColumns = 5)
        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            contentPadding = PaddingValues(AppDimensions.paddingMedium),
            horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall),
            verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall),
            modifier = modifier.fillMaxSize()
        ) {
            items(artists, key = { it.name }) { artist ->
                ArtistCardItem(
                    artist = artist,
                    onClick = { onArtistClick(artist) }
                )
            }
        }
    } else {
        if (isLandscape) {
            val columns = getGridColumnCount(true, portraitColumns = 1, landscapeColumns = 2)
            LazyVerticalGrid(
                columns = GridCells.Fixed(columns),
                contentPadding = PaddingValues(AppDimensions.paddingSmall),
                horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall),
                verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall),
                modifier = modifier.fillMaxSize()
            ) {
                items(artists, key = { it.name }) { artist ->
                    ArtistRowItem(
                        artist = artist,
                        onClick = { onArtistClick(artist) }
                    )
                }
            }
        } else {
            LazyColumn(modifier = modifier.fillMaxSize()) {
                items(artists, key = { it.name }) { artist ->
                    ArtistRowItem(
                        artist = artist,
                        onClick = { onArtistClick(artist) }
                    )
                }
            }
        }
    }
}
