package com.example.melora.core.designsystem.components.bottomsheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistAdd
import androidx.compose.material.icons.automirrored.filled.PlaylistPlay
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.example.melora.core.designsystem.components.MeloraBottomSheet
import com.example.melora.core.designsystem.components.MeloraCard
import com.example.melora.core.designsystem.components.MeloraIconButton
import com.example.melora.core.designsystem.dimensions
import com.example.melora.core.utils.formattedAlbumAndDuration
import com.example.melora.domain.model.Song

/**
 * Premium bottom sheet displaying more options and actions for a song.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongOptionsBottomSheet(
    song: Song,
    onDismiss: () -> Unit,
    onToggleFavorite: () -> Unit,
    onPlayNow: (() -> Unit)? = null,
    onPlayNext: (() -> Unit)? = null,
    onAddToQueue: (() -> Unit)? = null,
    onAddToPlaylist: () -> Unit,
    onViewAlbum: (() -> Unit)? = null,
    onViewArtist: (() -> Unit)? = null,
    onShare: () -> Unit,
    onSetAsRingtone: () -> Unit,
    onSongInfo: (() -> Unit)? = null,
    onRename: (() -> Unit)? = null,
    onDelete: (() -> Unit)? = null
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    MeloraBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = AppDimensions.paddingLarge)
        ) {
            // Enhanced Top Section: Hero Song Header Card with Metadata & Quick Actions
            MeloraCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppDimensions.paddingLarge, vertical = AppDimensions.paddingSmall),
                shape = RoundedCornerShape(MaterialTheme.dimensions.corners.large),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(AppDimensions.paddingMedium)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ArtworkImage(
                            artworkUri = song.artworkUri,
                            size = 60.dp,
                            cornerRadius = 12.dp
                        )
                        Spacer(modifier = Modifier.width(AppDimensions.paddingMedium))
                        Column(modifier = Modifier.weight(1f)) {
                            AppTypography.TitleMedium(
                                text = song.title,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(MaterialTheme.dimensions.spacing.micro))
                            AppTypography.BodySmall(
                                text = song.artist,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            AppTypography.LabelSmall(
                                text = song.formattedAlbumAndDuration,
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.85f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))

                    // Quick Action Shortcut Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        MeloraIconButton(onClick = onToggleFavorite) {
                            Icon(
                                imageVector = if (song.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = stringResource(R.string.cd_favorite),
                                tint = if (song.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        onPlayNow?.let { playNow ->
                            MeloraIconButton(onClick = {
                                onDismiss()
                                playNow()
                            }) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = stringResource(R.string.play_now),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        onPlayNext?.let { playNext ->
                            MeloraIconButton(onClick = {
                                onDismiss()
                                playNext()
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.QueueMusic,
                                    contentDescription = stringResource(R.string.play_next),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        MeloraIconButton(onClick = {
                            onDismiss()
                            onAddToPlaylist()
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.PlaylistAdd,
                                contentDescription = stringResource(R.string.add_to_playlist),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        MeloraIconButton(onClick = {
                            onDismiss()
                            onShare()
                        }) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = stringResource(R.string.share),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = AppDimensions.paddingExtraSmall),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
            )

            // Scrollable Actions List
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Favorite Action
                OptionItem(
                    icon = if (song.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    label = stringResource(if (song.isFavorite) R.string.remove_from_favorites else R.string.add_to_favorites),
                    iconTint = if (song.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    onClick = {
                        onDismiss()
                        onToggleFavorite()
                    }
                )

                onPlayNow?.let { playNow ->
                    OptionItem(
                        icon = Icons.Default.PlayArrow,
                        label = stringResource(R.string.play_now),
                        onClick = {
                            onDismiss()
                            playNow()
                        }
                    )
                }

                onPlayNext?.let { playNext ->
                    OptionItem(
                        icon = Icons.AutoMirrored.Filled.QueueMusic,
                        label = stringResource(R.string.play_next),
                        onClick = {
                            onDismiss()
                            playNext()
                        }
                    )
                }

                onAddToQueue?.let { addToQueue ->
                    OptionItem(
                        icon = Icons.AutoMirrored.Filled.PlaylistPlay,
                        label = stringResource(R.string.add_to_queue),
                        onClick = {
                            onDismiss()
                            addToQueue()
                        }
                    )
                }

                OptionItem(
                    icon = Icons.AutoMirrored.Filled.PlaylistAdd,
                    label = stringResource(R.string.add_to_playlist),
                    onClick = {
                        onDismiss()
                        onAddToPlaylist()
                    }
                )

                onViewAlbum?.let { viewAlbum ->
                    OptionItem(
                        icon = Icons.Default.Album,
                        label = stringResource(R.string.view_album),
                        onClick = {
                            onDismiss()
                            viewAlbum()
                        }
                    )
                }

                onViewArtist?.let { viewArtist ->
                    OptionItem(
                        icon = Icons.Default.Person,
                        label = stringResource(R.string.view_artist),
                        onClick = {
                            onDismiss()
                            viewArtist()
                        }
                    )
                }

                OptionItem(
                    icon = Icons.Default.NotificationsActive,
                    label = stringResource(R.string.set_as_ringtone),
                    onClick = {
                        onDismiss()
                        onSetAsRingtone()
                    }
                )

                OptionItem(
                    icon = Icons.Default.Share,
                    label = stringResource(R.string.share),
                    onClick = {
                        onDismiss()
                        onShare()
                    }
                )

                onSongInfo?.let { songInfo ->
                    OptionItem(
                        icon = Icons.Default.Info,
                        label = stringResource(R.string.song_info),
                        onClick = {
                            onDismiss()
                            songInfo()
                        }
                    )
                }

                onRename?.let { rename ->
                    OptionItem(
                        icon = Icons.Default.Edit,
                        label = stringResource(R.string.rename),
                        onClick = {
                            onDismiss()
                            rename()
                        }
                    )
                }

                onDelete?.let { deleteAction ->
                    OptionItem(
                        icon = Icons.Default.Delete,
                        label = stringResource(R.string.delete),
                        iconTint = MaterialTheme.colorScheme.error,
                        textColor = MaterialTheme.colorScheme.error,
                        onClick = {
                            onDismiss()
                            deleteAction()
                        }
                    )
                }
            }
        }
    }
}
