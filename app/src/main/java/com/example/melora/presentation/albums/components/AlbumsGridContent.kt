package com.example.melora.presentation.albums.components

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
import com.example.melora.domain.model.Album
import com.example.melora.core.designsystem.components.AlbumCardItem
import com.example.melora.core.designsystem.components.AlbumListItem
import com.example.melora.core.designsystem.AppDimensions

@Composable
fun AlbumsGridContent(
    albums: List<Album>,
    isLandscape: Boolean,
    isGridView: Boolean = true,
    onAlbumClick: (Album) -> Unit,
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
            items(albums, key = { it.id }) { album ->
                AlbumCardItem(
                    album = album,
                    onClick = { onAlbumClick(album) }
                )
            }
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(AppDimensions.paddingMedium),
            verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall),
            modifier = modifier.fillMaxSize()
        ) {
            items(albums, key = { it.id }) { album ->
                AlbumListItem(
                    album = album,
                    onClick = { onAlbumClick(album) }
                )
            }
        }
    }
}
