package com.example.melora.core.designsystem.components

import androidx.compose.foundation.clickable
import com.example.melora.core.utils.clickableDebounced
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.melora.R
import com.example.melora.domain.model.Album
import com.example.melora.core.designsystem.AppDimensions
import com.example.melora.core.designsystem.AppTypography

/**
 * Album item rendered as a row in list mode.
 */
@Composable
fun AlbumListItem(
    album: Album,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppDimensions.cardCornerRadius))
            .clickableDebounced { onClick() }
            .padding(vertical = AppDimensions.paddingSmall, horizontal = AppDimensions.paddingMedium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ArtworkImage(
            artworkUri = album.artworkUri,
            size = 56.dp,
            cornerRadius = AppDimensions.imageCornerRadius
        )

        Spacer(modifier = Modifier.width(AppDimensions.paddingMedium))

        Column(modifier = Modifier.weight(1f)) {
            AppTypography.TitleMedium(
                text = album.title,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(AppDimensions.paddingExtraSmall))
            AppTypography.BodySmall(
                text = stringResource(R.string.artist_and_songs_format, album.artist, album.songCount),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
