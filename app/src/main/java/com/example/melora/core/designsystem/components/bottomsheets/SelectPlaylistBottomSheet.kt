package com.example.melora.core.designsystem.components.bottomsheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.melora.R
import com.example.melora.core.designsystem.AppDimensions
import com.example.melora.core.designsystem.AppTypography
import com.example.melora.core.designsystem.components.MeloraBottomSheet
import com.example.melora.core.designsystem.components.MeloraTextButton
import com.example.melora.domain.model.Playlist

/**
 * Bottom sheet for selecting a target playlist to add a song to.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectPlaylistBottomSheet(
    playlists: List<Playlist>,
    onPlaylistSelected: (Long) -> Unit,
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
                .verticalScroll(rememberScrollState())
        ) {
            AppTypography.TitleLarge(
                text = stringResource(R.string.select_playlist),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = AppDimensions.paddingMedium)
            )

            if (playlists.isEmpty()) {
                AppTypography.BodyMedium(
                    text = stringResource(R.string.no_playlists_created),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                playlists.forEach { playlist ->
                    MeloraTextButton(
                        text = playlist.name,
                        onClick = { onPlaylistSelected(playlist.id) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(AppDimensions.paddingMedium))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                MeloraTextButton(
                    text = stringResource(R.string.cancel),
                    onClick = onDismiss
                )
            }
        }
    }
}
