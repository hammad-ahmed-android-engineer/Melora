package com.example.melora.core.designsystem.components

import androidx.compose.foundation.clickable
import com.example.melora.core.utils.clickableDebounced
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import com.example.melora.core.designsystem.components.MeloraCard
import com.example.melora.core.designsystem.components.MeloraIconButton
import com.example.melora.core.designsystem.components.MeloraLinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.melora.R
import com.example.melora.domain.model.PlaybackState
import com.example.melora.core.designsystem.AppDimensions
import com.example.melora.core.designsystem.AppTypography

/**
 * Bottom mini player bar displaying current playing song and quick playback controls.
 * In portrait: docked flush above navigation bar.
 * In landscape: elegant floating card with rounded corners and elevated card container color.
 */
@Composable
fun MiniPlayer(
    playbackState: PlaybackState,
    onClick: () -> Unit,
    onTogglePlayPause: () -> Unit,
    onSkipNext: () -> Unit,
    isLandscape: Boolean = false,
    modifier: Modifier = Modifier
) {
    val song = playbackState.currentSong ?: return

    val progress = if (playbackState.duration > 0) {
        (playbackState.currentPosition.toFloat() / playbackState.duration.toFloat()).coerceIn(0f, 1f)
    } else 0f

    // Flush docked mini-player at the bottom of the content container
    MeloraCard(
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        modifier = modifier
            .fillMaxWidth()
            .clickableDebounced { onClick() }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            MeloraLinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = AppDimensions.paddingMedium,
                        vertical = if (isLandscape) AppDimensions.paddingExtraSmall else AppDimensions.paddingSmall
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ArtworkImage(
                    artworkUri = song.artworkUri,
                    size = AppDimensions.components.avatar,
                    cornerRadius = AppDimensions.cornerSmall
                )

                Spacer(modifier = Modifier.width(AppDimensions.paddingMedium))

                Column(modifier = Modifier.weight(1f)) {
                    AppTypography.BodyMedium(
                        text = song.title,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    AppTypography.BodySmall(
                        text = song.artist,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                MeloraIconButton(onClick = onTogglePlayPause) {
                    Icon(
                        imageVector = if (playbackState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = stringResource(if (playbackState.isPlaying) R.string.pause else R.string.play),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                MeloraIconButton(onClick = onSkipNext) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = stringResource(R.string.next),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            if (!isLandscape) {
                HorizontalDivider(
                    thickness = 0.5.dp,
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                )
            }
        }
    }
}
