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
import androidx.compose.material.icons.filled.Lyrics
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.melora.R
import com.example.melora.core.designsystem.AppDimensions
import com.example.melora.core.designsystem.AppTypography
import com.example.melora.core.designsystem.components.ArtworkImage
import com.example.melora.core.designsystem.components.MeloraBottomSheet
import com.example.melora.core.designsystem.components.MeloraCard
import com.example.melora.core.designsystem.components.MeloraTextButton
import com.example.melora.core.utils.formattedArtistAndDuration
import com.example.melora.domain.model.Song

/**
 * Modern bottom sheet for displaying lyrics or fallback card for track metadata.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LyricsBottomSheet(
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
        ) {
            AppTypography.TitleLarge(
                text = stringResource(R.string.lyrics),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = AppDimensions.paddingSmall)
            )

            // Header: Song artwork, title, and artist
            MeloraCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                shape = RoundedCornerShape(AppDimensions.cornerSmall)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(AppDimensions.paddingSmall),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ArtworkImage(
                        artworkUri = song.artworkUri,
                        size = 44.dp,
                        cornerRadius = AppDimensions.cornerSmall
                    )
                    Spacer(modifier = Modifier.width(AppDimensions.paddingSmall))
                    Column(modifier = Modifier.weight(1f)) {
                        AppTypography.TitleSmall(
                            text = song.title,
                            fontWeight = FontWeight.Bold,
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
                }
            }

            Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))

            // Lyrics Content Card Container
            MeloraCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)),
                shape = RoundedCornerShape(AppDimensions.cornerMedium)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(AppDimensions.paddingMedium),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lyrics,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))
                    AppTypography.BodyMedium(
                        text = stringResource(R.string.no_lyrics_found),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
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
