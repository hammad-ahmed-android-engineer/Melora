package com.example.melora.presentation.songs.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.melora.R
import com.example.melora.core.designsystem.AppDimensions
import com.example.melora.presentation.songs.SongFilterTab

@Composable
fun SongFilterTabs(
    selectedTab: SongFilterTab,
    onTabSelected: (SongFilterTab) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = AppDimensions.paddingMedium, vertical = AppDimensions.paddingSmall),
        horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        item {
            FilterPill(
                text = stringResource(R.string.tab_all_songs),
                selected = selectedTab == SongFilterTab.ALL_SONGS,
                onClick = { onTabSelected(SongFilterTab.ALL_SONGS) }
            )
        }
        item {
            FilterPill(
                text = stringResource(R.string.tab_favorites),
                selected = selectedTab == SongFilterTab.FAVORITES,
                onClick = { onTabSelected(SongFilterTab.FAVORITES) }
            )
        }
    }
}
