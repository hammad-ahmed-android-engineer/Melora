package com.example.melora.core.designsystem.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.melora.R
import com.example.melora.core.designsystem.AppDimensions
import com.example.melora.core.designsystem.AppTypography
import com.example.melora.core.utils.clickableDebounced
import com.example.melora.domain.model.Artist

/**
 * Card item component for displaying an artist in grid layout.
 */
@Composable
fun ArtistCardItem(
    artist: Artist,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    MeloraCard(
        modifier = modifier
            .fillMaxWidth()
            .clickableDebounced { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)),
        shape = RoundedCornerShape(AppDimensions.cornerMedium)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppDimensions.paddingSmall),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ArtworkImage(
                artworkUri = artist.artworkUri,
                size = 110.dp,
                cornerRadius = AppDimensions.cornerPill
            )
            Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))
            AppTypography.TitleSmall(
                text = artist.name,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            AppTypography.BodySmall(
                text = stringResource(R.string.songs_count_format, artist.songCount),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
