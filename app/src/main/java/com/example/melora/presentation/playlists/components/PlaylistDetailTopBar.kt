package com.example.melora.presentation.playlists.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import com.example.melora.core.designsystem.components.MeloraIconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.melora.R
import com.example.melora.core.designsystem.AppTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistDetailTopBar(
    playlistName: String,
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    TopAppBar(
        title = { AppTypography.TitleLarge(text = playlistName) },
        navigationIcon = {
            MeloraIconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
        },
        actions = {
            MeloraIconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.delete),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    )
}
