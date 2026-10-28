package com.example.melora.core.designsystem.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import com.example.melora.core.designsystem.components.MeloraIconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.example.melora.R
import com.example.melora.core.utils.clickableDebounced
import com.example.melora.domain.model.Playlist
import com.example.melora.core.designsystem.AppDimensions
import com.example.melora.core.designsystem.AppTypography

/**
 * Playlist row item for displaying playlists in list layouts.
 */
@Composable
fun PlaylistRowItem(
    playlist: Playlist,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickableDebounced { onClick() }
            .padding(horizontal = AppDimensions.paddingMedium, vertical = AppDimensions.paddingSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ArtworkImage(
            artworkUri = playlist.artworkUri,
            size = AppDimensions.artworkSmall,
            cornerRadius = AppDimensions.cornerSmall
        )
        Spacer(modifier = Modifier.width(AppDimensions.paddingMedium))
        Column(modifier = Modifier.weight(1f)) {
            AppTypography.TitleMedium(
                text = playlist.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            AppTypography.BodySmall(
                text = stringResource(R.string.songs_count_format, playlist.songCount),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        MeloraIconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(R.string.delete),
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}
