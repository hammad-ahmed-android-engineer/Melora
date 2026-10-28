package com.example.melora.presentation.playlists.components

import androidx.compose.foundation.clickable
import com.example.melora.core.utils.clickableDebounced
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import com.example.melora.core.designsystem.components.MeloraTextButton
import com.example.melora.core.designsystem.components.MeloraDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.melora.R
import com.example.melora.domain.model.Song
import com.example.melora.core.designsystem.components.ArtworkImage
import com.example.melora.core.designsystem.AppDimensions
import com.example.melora.core.designsystem.AppTypography
import com.example.melora.core.designsystem.dimensions

/**
 * Dialog allowing user to select a song from library to add into the current playlist.
 */
@Composable
fun AddSongsToPlaylistDialog(
    availableSongs: List<Song>,
    existingSongIds: Set<Long>,
    onSongSelected: (Song) -> Unit,
    onDismiss: () -> Unit
) {
    MeloraDialog(
        onDismissRequest = onDismiss,
        title = stringResource(R.string.add_songs)
    ) {
        if (availableSongs.isEmpty()) {
            AppTypography.BodyMedium(
                text = stringResource(R.string.no_songs_found),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
            ) {
                items(availableSongs, key = { it.id }) { song ->
                    val isAlreadyIn = existingSongIds.contains(song.id)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickableDebounced(enabled = !isAlreadyIn) {
                                onSongSelected(song)
                            }
                            .padding(vertical = AppDimensions.paddingSmall),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ArtworkImage(
                            artworkUri = song.artworkUri,
                            size = 44.dp,
                            cornerRadius = AppDimensions.cornerSmall
                        )

                        Spacer(modifier = Modifier.width(AppDimensions.paddingSmall))

                        Column(modifier = Modifier.weight(1f)) {
                            AppTypography.BodyMedium(
                                text = song.title,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isAlreadyIn) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f) else MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            AppTypography.LabelSmall(
                                text = song.artist,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = if (isAlreadyIn) 0.4f else 0.8f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        if (isAlreadyIn) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(MaterialTheme.dimensions.spacing.default)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = stringResource(R.string.add_songs),
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(MaterialTheme.dimensions.spacing.default)
                            )
                        }
                    }
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            MeloraTextButton(
                text = stringResource(R.string.close),
                onClick = onDismiss
            )
        }
    }
}
