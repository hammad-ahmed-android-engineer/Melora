package com.example.melora.presentation.albums.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistAdd
import androidx.compose.material.icons.automirrored.filled.PlaylistPlay
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import com.example.melora.core.designsystem.components.MeloraOutlinedButton
import com.example.melora.core.designsystem.components.MeloraButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.melora.R
import com.example.melora.domain.model.Album
import com.example.melora.domain.model.Song
import com.example.melora.core.designsystem.components.ArtworkImage
import com.example.melora.core.designsystem.components.SongRowItem
import com.example.melora.core.designsystem.AppDimensions
import com.example.melora.core.designsystem.AppTypography

@Composable
fun AlbumDetailContent(
    album: Album,
    songs: List<Song>,
    onPlayAll: () -> Unit,
    onShuffleAll: () -> Unit,
    onSongClick: (Song) -> Unit,
    onAddToQueue: (Song) -> Unit = {},
    onAddToPlaylist: (Song) -> Unit = {},
    onAddAlbumToQueue: () -> Unit = {},
    onAddAlbumToPlaylist: () -> Unit = {},
    onToggleFavorite: (Long) -> Unit = {},
    currentPlayingSongId: Long? = null,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = AppDimensions.paddingExtraLarge),
        modifier = modifier.fillMaxSize()
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppDimensions.paddingMedium),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Large Album Artwork
                val artworkSize = 180.dp
                ArtworkImage(
                    artworkUri = album.artworkUri,
                    size = artworkSize,
                    cornerRadius = AppDimensions.cardCornerRadius
                )

                Spacer(modifier = Modifier.height(AppDimensions.paddingMedium))

                // Title & Details
                AppTypography.TitleLarge(
                    text = album.title,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(AppDimensions.paddingExtraSmall))
                val subtitle = buildString {
                    append(album.artist)
                    if (album.year > 0) append(" â€¢ ${album.year}")
                    append(" â€¢ ${songs.size} songs")
                }
                AppTypography.BodyMedium(
                    text = subtitle,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(AppDimensions.paddingMedium))

                // Prominent Play & Shuffle Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
                ) {
                    MeloraButton(
                        text = stringResource(R.string.play),
                        onClick = onPlayAll,
                        modifier = Modifier.weight(1f)
                    )

                    MeloraOutlinedButton(
                        onClick = onShuffleAll,
                        modifier = Modifier
                            .weight(1f)
                            .height(AppDimensions.buttonHeightMedium),
                        shape = RoundedCornerShape(50)
                    ) {
                        Icon(imageVector = Icons.Default.Shuffle, contentDescription = null)
                        Spacer(modifier = Modifier.width(AppDimensions.paddingSmall))
                        AppTypography.TitleSmall(
                            text = stringResource(R.string.shuffle),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))

                // Quick Actions: Add to Queue & Add to Playlist
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    MeloraOutlinedButton(
                        onClick = onAddAlbumToQueue,
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.padding(end = AppDimensions.paddingSmall)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.PlaylistPlay,
                            contentDescription = null,
                            modifier = Modifier.size(AppDimensions.iconSizeSmall)
                        )
                        Spacer(modifier = Modifier.width(AppDimensions.paddingExtraSmall))
                        AppTypography.LabelMedium(text = stringResource(R.string.add_to_queue))
                    }

                    MeloraOutlinedButton(
                        onClick = onAddAlbumToPlaylist,
                        shape = RoundedCornerShape(50)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.PlaylistAdd,
                            contentDescription = null,
                            modifier = Modifier.size(AppDimensions.iconSizeSmall)
                        )
                        Spacer(modifier = Modifier.width(AppDimensions.paddingExtraSmall))
                        AppTypography.LabelMedium(text = stringResource(R.string.add_to_playlist))
                    }
                }
            }
        }

        // Vertical List of Tracks
        itemsIndexed(songs, key = { _, song -> song.id }) { index, song ->
            SongRowItem(
                song = song,
                onClick = { onSongClick(song) },
                onToggleFavorite = { onToggleFavorite(song.id) },
                onAddToPlaylist = { onAddToPlaylist(song) },
                onPlayNext = {},
                onAddToQueue = { onAddToQueue(song) }
            )
        }
    }
}

