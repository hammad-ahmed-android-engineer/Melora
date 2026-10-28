package com.example.melora.core.designsystem.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.example.melora.R
import com.example.melora.core.utils.clickableDebounced
import com.example.melora.domain.model.AudioFolder
import com.example.melora.core.designsystem.AppDimensions
import com.example.melora.core.designsystem.AppTypography

/**
 * Reusable list item component for displaying audio folder details.
 */
@Composable
fun FolderRowItem(
    folder: AudioFolder,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickableDebounced { onClick() }
            .padding(horizontal = AppDimensions.paddingMedium, vertical = AppDimensions.paddingSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Folder,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(AppDimensions.paddingMedium))
        Column(modifier = Modifier.weight(1f)) {
            AppTypography.TitleMedium(
                text = folder.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            AppTypography.BodySmall(
                text = "${stringResource(R.string.songs_count_format, folder.songCount)} \u2022 ${folder.path}",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
