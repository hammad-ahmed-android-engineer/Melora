package com.example.melora.presentation.playlists.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.melora.R
import com.example.melora.core.designsystem.AppTypography
import com.example.melora.core.designsystem.components.MeloraIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistsTopBar(
    playlistsCount: Int = 0,
    isGridView: Boolean,
    onToggleView: () -> Unit,
    onCreatePlaylist: () -> Unit
) {
    TopAppBar(
        title = {
            AppTypography.TitleLarge(
                text = stringResource(R.string.playlists_header, playlistsCount),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        actions = {
            MeloraIconButton(onClick = onToggleView) {
                Icon(
                    imageVector = if (isGridView) Icons.Default.ViewAgenda else Icons.Default.GridView,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            MeloraIconButton(onClick = onCreatePlaylist) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.new_playlist),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    )
}
