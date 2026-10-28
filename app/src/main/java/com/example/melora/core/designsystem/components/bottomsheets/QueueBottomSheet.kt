package com.example.melora.core.designsystem.components.bottomsheets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import com.example.melora.core.designsystem.components.MeloraBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.melora.R
import com.example.melora.domain.model.Song
import com.example.melora.core.designsystem.AppDimensions
import com.example.melora.core.designsystem.AppTypography

/**
 * Bottom sheet displaying current playback queue items.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QueueBottomSheet(
    queue: List<Song>,
    currentIndex: Int,
    sheetState: SheetState,
    onDismiss: () -> Unit
) {
    MeloraBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(modifier = Modifier.padding(AppDimensions.paddingDefault)) {
            AppTypography.TitleMedium(
                text = stringResource(R.string.current_queue, queue.size)
            )
            Spacer(modifier = Modifier.height(AppDimensions.paddingMedium))
            LazyColumn {
                itemsIndexed(queue, key = { index, song -> "queue_${index}_${song.id}" }) { index, songItem ->
                    val isCurrent = index == currentIndex
                    AppTypography.BodyMedium(
                        text = "${index + 1}. ${songItem.title} - ${songItem.artist}",
                        color = if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(vertical = AppDimensions.paddingTiny)
                    )
                }
            }
        }
    }
}
