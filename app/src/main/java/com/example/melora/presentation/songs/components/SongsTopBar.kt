package com.example.melora.presentation.songs.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.melora.R
import com.example.melora.core.designsystem.AppTypography
import com.example.melora.core.designsystem.components.MeloraIconButton
import com.example.melora.core.designsystem.components.MeloraTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongsTopBar(
    songsCount: Int,
    isMultiSelectMode: Boolean,
    selectedCount: Int,
    isAllSelected: Boolean,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onRefresh: () -> Unit,
    onOpenSort: () -> Unit,
    onShuffleAll: () -> Unit,
    onToggleSelectAll: () -> Unit,
    onCancelSelection: () -> Unit,
    onShareSelected: () -> Unit = {},
    onDeleteSelected: () -> Unit = {}
) {
    var isSearchExpanded by remember { mutableStateOf(false) }

    if (isMultiSelectMode) {
        TopAppBar(
            title = {
                AppTypography.TitleMedium(
                    text = stringResource(R.string.selected_count, selectedCount),
                    fontWeight = FontWeight.Bold
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            navigationIcon = {
                MeloraIconButton(onClick = onCancelSelection) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.cancel)
                    )
                }
            },
            actions = {
                if (selectedCount > 0) {
                    MeloraIconButton(onClick = onShareSelected) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = stringResource(R.string.share)
                        )
                    }
                    MeloraIconButton(onClick = onDeleteSelected) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(R.string.delete),
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
                MeloraIconButton(onClick = onToggleSelectAll) {
                    Icon(
                        imageVector = if (isAllSelected) Icons.Default.ClearAll else Icons.Default.SelectAll,
                        contentDescription = if (isAllSelected) "Deselect all" else stringResource(R.string.select_all)
                    )
                }
            }
        )
    } else if (isSearchExpanded) {
        TopAppBar(
            title = {
                MeloraTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = stringResource(R.string.search_placeholder),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
            navigationIcon = {
                MeloraIconButton(onClick = {
                    isSearchExpanded = false
                    onSearchQueryChange("")
                }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.close)
                    )
                }
            }
        )
    } else {
        TopAppBar(
            title = {
                AppTypography.TitleLarge(
                    text = stringResource(R.string.all_songs_header, songsCount),
                    fontWeight = FontWeight.Bold
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
            actions = {
                MeloraIconButton(onClick = { isSearchExpanded = true }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.nav_search)
                    )
                }
                MeloraIconButton(onClick = onShuffleAll) {
                    Icon(
                        imageVector = Icons.Default.Shuffle,
                        contentDescription = stringResource(R.string.shuffle_all),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                MeloraIconButton(onClick = onRefresh) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = stringResource(R.string.refresh)
                    )
                }
                MeloraIconButton(onClick = onOpenSort) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Sort,
                        contentDescription = stringResource(R.string.sort)
                    )
                }
            }
        )
    }
}
