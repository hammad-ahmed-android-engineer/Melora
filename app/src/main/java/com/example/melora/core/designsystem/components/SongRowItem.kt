package com.example.melora.core.designsystem.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.RadioButtonUnchecked
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.example.melora.R
import com.example.melora.core.designsystem.AppDimensions
import com.example.melora.core.designsystem.AppTypography
import com.example.melora.core.utils.formattedArtistAndDuration
import com.example.melora.core.utils.formattedDuration
import com.example.melora.core.utils.rememberDebouncedClick
import com.example.melora.core.utils.setAsRingtone
import com.example.melora.core.utils.shareAudioTrack
import com.example.melora.domain.model.Song
import com.example.melora.core.designsystem.components.bottomsheets.SongOptionsBottomSheet

/**
 * Standard song item row composable supporting multi-selection and bottom-sheet song action menu.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SongRowItem(
    song: Song,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    onAddToPlaylist: () -> Unit,
    onPlayNext: (() -> Unit)? = null,
    onAddToQueue: (() -> Unit)? = null,
    onPlayNow: (() -> Unit)? = onClick,
    onViewAlbum: (() -> Unit)? = null,
    onViewArtist: (() -> Unit)? = null,
    onShare: (() -> Unit)? = null,
    onShowInfo: (() -> Unit)? = null,
    onSongInfo: (() -> Unit)? = onShowInfo,
    onRename: (() -> Unit)? = null,
    onDelete: (() -> Unit)? = null,
    isSelected: Boolean = false,
    isMultiSelectMode: Boolean = false,
    onToggleSelect: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val effectiveInfo = onSongInfo ?: onShowInfo
    var showBottomSheet by remember { mutableStateOf(false) }

    val debouncedClick = rememberDebouncedClick {
        if (isMultiSelectMode && onToggleSelect != null) {
            onToggleSelect()
        } else {
            onClick()
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = debouncedClick,
                onLongClick = onLongClick
            )
            .padding(horizontal = AppDimensions.paddingMedium, vertical = AppDimensions.paddingSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isMultiSelectMode) {
            MeloraIconButton(onClick = { onToggleSelect?.invoke() }) {
                Icon(
                    imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                    contentDescription = null,
                    tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        ArtworkImage(
            artworkUri = song.artworkUri,
            size = AppDimensions.artworkSmall,
            cornerRadius = AppDimensions.cornerMedium
        )
        Spacer(modifier = Modifier.width(AppDimensions.paddingMedium))
        Column(modifier = Modifier.weight(1f)) {
            AppTypography.TitleSmall(
                text = song.title,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            AppTypography.BodySmall(
                text = song.formattedArtistAndDuration,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (!isMultiSelectMode) {
            MeloraIconButton(onClick = { showBottomSheet = true }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(R.string.cd_more_options),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

    if (showBottomSheet) {
        SongOptionsBottomSheet(
            song = song,
            onDismiss = { showBottomSheet = false },
            onToggleFavorite = onToggleFavorite,
            onPlayNow = onPlayNow,
            onPlayNext = onPlayNext,
            onAddToQueue = onAddToQueue,
            onAddToPlaylist = onAddToPlaylist,
            onViewAlbum = onViewAlbum,
            onViewArtist = onViewArtist,
            onShare = {
                if (onShare != null) {
                    onShare()
                } else {
                    context.shareAudioTrack(song)
                }
            },
            onSetAsRingtone = {
                context.setAsRingtone(song)
            },
            onSongInfo = effectiveInfo,
            onRename = onRename,
            onDelete = onDelete
        )
    }
}
