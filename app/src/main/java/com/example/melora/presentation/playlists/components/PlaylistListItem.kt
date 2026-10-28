package com.example.melora.presentation.playlists.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import com.example.melora.core.utils.clickableDebounced
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import com.example.melora.core.designsystem.components.MeloraIconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.melora.R
import com.example.melora.domain.model.Playlist
import com.example.melora.core.designsystem.AppDimensions
import com.example.melora.core.designsystem.AppTypography

@Composable
fun PlaylistListItem(
    playlist: Playlist,
    onClick: () -> Unit,
    onPlay: () -> Unit,
    onShuffle: () -> Unit,
    onDuplicate: () -> Unit,
    onDelete: () -> Unit,
    isBuiltIn: Boolean = false,
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }

    // Curated gradients matching screenshot 4
    val gradient = when {
        playlist.name.contains("Favorite", ignoreCase = true) -> listOf(
            Color(0xFF2E0854), Color(0xFFC026D3)
        )
        playlist.name.contains("Night", ignoreCase = true) -> listOf(
            Color(0xFFF59E0B), Color(0xFFFB923C)
        )
        playlist.name.contains("Focus", ignoreCase = true) -> listOf(
            Color(0xFF0F172A), Color(0xFF06B6D4)
        )
        playlist.id % 3L == 0L -> listOf(Color(0xFF1E3A8A), Color(0xFF3B82F6))
        playlist.id % 3L == 1L -> listOf(Color(0xFF065F46), Color(0xFF34D399))
        else -> listOf(Color(0xFF831843), Color(0xFFF43F5E))
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppDimensions.cardCornerRadius))
            .clickableDebounced { onClick() }
            .padding(horizontal = AppDimensions.paddingMedium, vertical = AppDimensions.paddingSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(AppDimensions.imageCornerRadius))
                .background(Brush.verticalGradient(gradient))
        )

        Spacer(modifier = Modifier.width(AppDimensions.paddingMedium))

        Column(modifier = Modifier.weight(1f)) {
            AppTypography.TitleMedium(
                text = playlist.name,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(AppDimensions.paddingExtraSmall))
            val subtitle = if (isBuiltIn) {
                "${playlist.songCount} songs \u2022 " + stringResource(R.string.builtin_playlist)
            } else {
                "${playlist.songCount} songs \u2022 " + stringResource(R.string.your_playlist)
            }
            AppTypography.BodySmall(
                text = subtitle,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
        }

        Box {
            MeloraIconButton(onClick = { showMenu = true }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(R.string.cd_more_options)
                )
            }

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = { AppTypography.BodyMedium(text = stringResource(R.string.play)) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                    },
                    onClick = {
                        showMenu = false
                        onPlay()
                    }
                )
                DropdownMenuItem(
                    text = { AppTypography.BodyMedium(text = stringResource(R.string.shuffle)) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Shuffle, contentDescription = null)
                    },
                    onClick = {
                        showMenu = false
                        onShuffle()
                    }
                )
                DropdownMenuItem(
                    text = { AppTypography.BodyMedium(text = stringResource(R.string.duplicate_playlist)) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null)
                    },
                    onClick = {
                        showMenu = false
                        onDuplicate()
                    }
                )
                if (!isBuiltIn) {
                    DropdownMenuItem(
                        text = {
                            AppTypography.BodyMedium(
                                text = stringResource(R.string.delete),
                                color = MaterialTheme.colorScheme.error
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            )
                        },
                        onClick = {
                            showMenu = false
                            onDelete()
                        }
                    )
                }
            }
        }
    }
}
