package com.example.melora.presentation.player.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import com.example.melora.core.designsystem.components.MeloraIconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.melora.R
import com.example.melora.core.utils.clickableDebounced
import com.example.melora.domain.model.RepeatMode
import com.example.melora.core.designsystem.AppDimensions
import com.example.melora.core.designsystem.dimensions

@Composable
fun PlayerControlsSection(
    isPlaying: Boolean,
    isShuffleEnabled: Boolean,
    repeatMode: RepeatMode,
    onToggleShuffle: () -> Unit,
    onSkipPrevious: () -> Unit,
    onTogglePlayPause: () -> Unit,
    onSkipNext: () -> Unit,
    onToggleRepeat: () -> Unit,
    onRewind10: (() -> Unit)? = null,
    onForward10: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Shuffle with active dot indicator
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            MeloraIconButton(onClick = onToggleShuffle) {
                Icon(
                    imageVector = Icons.Default.Shuffle,
                    contentDescription = stringResource(R.string.shuffle),
                    tint = if (isShuffleEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (isShuffleEnabled) {
                Box(
                    modifier = Modifier
                        .size(MaterialTheme.dimensions.spacing.tiny)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                )
            }
        }

        // Rewind 10s or Previous
        if (onRewind10 != null) {
            MeloraIconButton(onClick = onRewind10) {
                Icon(
                    imageVector = Icons.Default.Replay10,
                    contentDescription = stringResource(R.string.rewind_10),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        MeloraIconButton(
            onClick = onSkipPrevious,
            modifier = Modifier.size(MaterialTheme.dimensions.icons.large)
        ) {
            Icon(
                imageVector = Icons.Default.SkipPrevious,
                contentDescription = stringResource(R.string.previous),
                modifier = Modifier.size(MaterialTheme.dimensions.icons.medium),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        // Prominent FAB-style Play/Pause Button
        Box(
            modifier = Modifier
                .size(68.dp)
                .shadow(
                    elevation = AppDimensions.elevations.high,
                    shape = CircleShape
                )
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .clickableDebounced { onTogglePlayPause() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = stringResource(if (isPlaying) R.string.pause else R.string.play),
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(36.dp)
            )
        }

        MeloraIconButton(
            onClick = onSkipNext,
            modifier = Modifier.size(MaterialTheme.dimensions.icons.large)
        ) {
            Icon(
                imageVector = Icons.Default.SkipNext,
                contentDescription = stringResource(R.string.next),
                modifier = Modifier.size(MaterialTheme.dimensions.icons.medium),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        // Forward 10s
        if (onForward10 != null) {
            MeloraIconButton(onClick = onForward10) {
                Icon(
                    imageVector = Icons.Default.Forward10,
                    contentDescription = stringResource(R.string.forward_10),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // Repeat with active dot indicator
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            MeloraIconButton(onClick = onToggleRepeat) {
                Icon(
                    imageVector = when (repeatMode) {
                        RepeatMode.ONE -> Icons.Default.RepeatOne
                        else -> Icons.Default.Repeat
                    },
                    contentDescription = stringResource(R.string.repeat),
                    tint = if (repeatMode != RepeatMode.OFF) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (repeatMode != RepeatMode.OFF) {
                Box(
                    modifier = Modifier
                        .size(MaterialTheme.dimensions.spacing.tiny)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                )
            }
        }
    }
}
