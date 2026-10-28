package com.example.melora.core.designsystem.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.example.melora.R
import com.example.melora.core.designsystem.AppDimensions
import com.example.melora.core.designsystem.AppTypography
import com.example.melora.core.utils.clickableDebounced
import com.example.melora.domain.model.Playlist

/**
 * Playlist card item for displaying playlists in responsive grid layouts.
 */
@Composable
fun PlaylistCardItem(
    playlist: Playlist,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    MeloraCard(
        modifier = modifier
            .fillMaxWidth()
            .clickableDebounced { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Column(modifier = Modifier.padding(AppDimensions.paddingSmall)) {
            ArtworkImage(
                artworkUri = playlist.artworkUri,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                size = AppDimensions.artworkMedium,
                cornerRadius = AppDimensions.cornerMedium
            )
            Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))
            AppTypography.TitleMedium(
                text = playlist.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            AppTypography.BodySmall(
                text = stringResource(R.string.songs_count_format, playlist.songCount),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
