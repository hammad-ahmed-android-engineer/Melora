package com.example.melora.presentation.songs.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import com.example.melora.core.designsystem.components.ArtworkImage
import com.example.melora.core.designsystem.components.MeloraCard
import com.example.melora.core.utils.clickableDebounced
import com.example.melora.core.utils.formattedArtistAndDuration
import com.example.melora.domain.model.Song

/**
 * Header section displayed at the top of the All Songs list showing up to 3 recently played songs
 * in a horizontal scrollable row layout.
 */
@Composable
fun RecentlyPlayedTopSection(
    songs: List<Song>,
    onSongClick: (Song) -> Unit,
    modifier: Modifier = Modifier
) {
    if (songs.isEmpty()) return

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingExtraSmall)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = AppDimensions.paddingMedium, vertical = AppDimensions.paddingSmall)
        ) {
            Icon(
                imageVector = Icons.Default.History,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            AppTypography.LabelMedium(
                text = stringResource(R.string.recently_played),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = AppDimensions.paddingMedium),
            horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(songs.take(3), key = { "recent_${it.id}" }) { song ->
                MeloraCard(
                    modifier = Modifier
                        .width(200.dp)
                        .clickableDebounced { onSongClick(song) },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                    ),
                    shape = RoundedCornerShape(AppDimensions.cornerSmall)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(AppDimensions.paddingSmall),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ArtworkImage(
                            artworkUri = song.artworkUri,
                            size = 48.dp,
                            cornerRadius = AppDimensions.cornerSmall
                        )
                        Spacer(modifier = Modifier.width(AppDimensions.paddingSmall))
                        Column(modifier = Modifier.weight(1f)) {
                            AppTypography.BodySmall(
                                text = song.title,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            AppTypography.LabelSmall(
                                text = song.formattedArtistAndDuration,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))
    }
}
