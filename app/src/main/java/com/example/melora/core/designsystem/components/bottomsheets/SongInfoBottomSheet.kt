package com.example.melora.core.designsystem.components.bottomsheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.melora.core.designsystem.components.MeloraTextButton
import com.example.melora.core.utils.formattedDuration
import com.example.melora.core.utils.formattedSize
import com.example.melora.domain.model.Song

/**
 * Modern bottom sheet displaying detailed metadata for a song with structured info cards.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongInfoBottomSheet(
    song: Song,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
    MeloraBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppDimensions.paddingLarge)
                .padding(bottom = AppDimensions.paddingLarge)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)
        ) {
            AppTypography.TitleLarge(
                text = stringResource(R.string.song_info),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = AppDimensions.paddingExtraSmall)
            )

            // Header card with Artwork, Title, and Artist
            MeloraCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(AppDimensions.cornerMedium)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(AppDimensions.paddingMedium),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ArtworkImage(
                        artworkUri = song.artworkUri,
                        size = 52.dp,
                        cornerRadius = AppDimensions.cornerSmall
                    )
                    Spacer(modifier = Modifier.width(AppDimensions.paddingMedium))
                    Column(modifier = Modifier.weight(1f)) {
                        AppTypography.TitleSmall(
                            text = song.title,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        AppTypography.BodySmall(
                            text = song.artist,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            // Info items container
            MeloraCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)),
                shape = RoundedCornerShape(AppDimensions.cornerMedium)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    InfoRowItem(
                        icon = Icons.Default.MusicNote,
                        label = stringResource(R.string.info_label_title),
                        value = song.title
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    InfoRowItem(
                        icon = Icons.Default.Person,
                        label = stringResource(R.string.info_label_artist),
                        value = song.artist
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    InfoRowItem(
                        icon = Icons.Default.Album,
                        label = stringResource(R.string.info_label_album),
                        value = song.album
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    InfoRowItem(
                        icon = Icons.Default.Schedule,
                        label = stringResource(R.string.info_label_duration),
                        value = song.formattedDuration
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    InfoRowItem(
                        icon = Icons.Default.Storage,
                        label = stringResource(R.string.info_label_size),
                        value = song.formattedSize
                    )
                    if (!song.filePath.isNullOrBlank()) {
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                        InfoRowItem(
                            icon = Icons.Default.Folder,
                            label = stringResource(R.string.info_label_path),
                            value = song.filePath
                        )
                    }
                }
            }
            InfoRowItem(
                icon = Icons.Default.Person,
                label = stringResource(R.string.info_label_artist),
                value = song.artist
            )
            InfoRowItem(
                icon = Icons.Default.Album,
                label = stringResource(R.string.info_label_album),
                value = song.album
            )
            InfoRowItem(
                icon = Icons.Default.Schedule,
                label = stringResource(R.string.info_label_duration),
                value = song.formattedDuration
            )
            InfoRowItem(
                icon = Icons.Default.Storage,
                label = stringResource(R.string.info_label_size),
                value = song.formattedSize
            )
            if (!song.filePath.isNullOrBlank()) {
                InfoRowItem(
                    icon = Icons.Default.Folder,
                    label = stringResource(R.string.info_label_path),
                    value = song.filePath
                )
            }

            Spacer(modifier = Modifier.height(AppDimensions.paddingMedium))

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
}

@Composable
private fun InfoRowItem(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = AppDimensions.paddingMedium, vertical = AppDimensions.paddingSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(AppDimensions.paddingMedium))
        Column(modifier = Modifier.weight(1f)) {
            AppTypography.LabelSmall(
                text = label,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            AppTypography.BodyMedium(
                text = value,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
