package com.example.melora.core.designsystem.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.example.melora.R
import com.example.melora.core.utils.clickableDebounced
import com.example.melora.domain.model.Artist
import com.example.melora.core.designsystem.AppDimensions
import com.example.melora.core.designsystem.AppTypography

/**
 * Artist row composable displayed in artist lists.
 */
@Composable
fun ArtistRowItem(
    artist: Artist,
    onClick: () -> Unit,
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
            artworkUri = artist.artworkUri,
            size = AppDimensions.artworkSmall,
            cornerRadius = AppDimensions.cornerPill
        )
        Spacer(modifier = Modifier.width(AppDimensions.paddingMedium))
        Column(modifier = Modifier.weight(1f)) {
            AppTypography.TitleMedium(
                text = artist.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            AppTypography.BodySmall(
                text = stringResource(R.string.songs_and_albums_format, artist.songCount, artist.albumCount),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
