package com.example.melora.presentation.player.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.melora.R
import com.example.melora.core.designsystem.AppDimensions
import com.example.melora.core.designsystem.AppTypography
import com.example.melora.core.designsystem.components.MeloraIconButton
import com.example.melora.core.designsystem.dimensions

/**
 * Rich bottom action toolbar for Player screen providing quick access to
 * Playback Queue (left) and Audio Equalizer (right).
 */
@Composable
fun PlayerBottomToolbar(
    queueSize: Int,
    onOpenQueue: () -> Unit,
    onOpenEqualizer: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = AppDimensions.paddingLarge, vertical = AppDimensions.paddingSmall),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 1. Playback Queue (Left icon) with non-cropping badge
        MeloraIconButton(onClick = onOpenQueue) {
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(MaterialTheme.dimensions.spacing.tiny),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.QueueMusic,
                    contentDescription = stringResource(R.string.cd_queue),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (queueSize > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 8.dp, y = (-6).dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                            .sizeIn(minWidth = 16.dp, minHeight = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        AppTypography.BadgeText(
                            text = if (queueSize > 99) "99+" else queueSize.toString(),
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        // 2. Audio Equalizer & Sound FX (Right icon)
        MeloraIconButton(onClick = onOpenEqualizer) {
            Icon(
                imageVector = Icons.Default.Equalizer,
                contentDescription = stringResource(R.string.cd_equalizer),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
