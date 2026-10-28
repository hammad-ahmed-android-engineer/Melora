package com.example.melora.presentation.albums.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import com.example.melora.core.designsystem.components.MeloraIconButton
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
import com.example.melora.core.designsystem.components.MeloraTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumsTopBar(
    albumsCount: Int,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    isGridView: Boolean,
    onToggleView: () -> Unit
) {
    var isSearchExpanded by remember { mutableStateOf(false) }

    if (isSearchExpanded) {
        TopAppBar(
            title = {
                MeloraTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = stringResource(R.string.search_albums),
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
                    text = stringResource(R.string.albums_header, albumsCount),
                    fontWeight = FontWeight.Bold
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
            actions = {
                MeloraIconButton(onClick = { isSearchExpanded = true }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.search_albums)
                    )
                }
                MeloraIconButton(onClick = onToggleView) {
                    Icon(
                        imageVector = if (isGridView) Icons.Default.ViewAgenda else Icons.Default.GridView,
                        contentDescription = null
                    )
                }
            }
        )
    }
}

