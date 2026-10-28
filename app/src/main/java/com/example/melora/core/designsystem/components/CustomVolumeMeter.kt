package com.example.melora.core.designsystem.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeDown
import androidx.compose.material.icons.automirrored.filled.VolumeMute
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.melora.core.designsystem.AppTypography
import com.example.melora.core.designsystem.dimensions
import kotlin.math.roundToInt

/**
 * Premium in-app floating volume meter HUD displayed when hardware volume buttons are pressed.
 */
@Composable
fun CustomVolumeMeter(
    volumePercent: Float,
    isVisible: Boolean,
    onVolumeChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .zIndex(100f)
            .padding(top = 44.dp, start = MaterialTheme.dimensions.spacing.default, end = MaterialTheme.dimensions.spacing.default),
        contentAlignment = Alignment.TopCenter
    ) {
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut()
        ) {
            Row(
                modifier = Modifier
                    .shadow(elevation = 12.dp, shape = CircleShape)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.94f))
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                        shape = CircleShape
                    )
                    .padding(horizontal = MaterialTheme.dimensions.spacing.medium, vertical = 6.dp)
                    .width(280.dp)
                    .height(48.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Dynamic Volume Icon based on current percent
                val volumeIcon = when {
                    volumePercent <= 0.02f -> Icons.AutoMirrored.Filled.VolumeMute
                    volumePercent < 0.5f -> Icons.AutoMirrored.Filled.VolumeDown
                    else -> Icons.AutoMirrored.Filled.VolumeUp
                }

                Icon(
                    imageVector = volumeIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(22.dp)
                )

                Spacer(modifier = Modifier.width(MaterialTheme.dimensions.spacing.small))

                // Interactive slider
                MeloraSlider(
                    value = volumePercent.coerceIn(0f, 1f),
                    onValueChange = onVolumeChange,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(MaterialTheme.dimensions.spacing.small))

                // Percentage display
                AppTypography.LabelMedium(
                    text = "${(volumePercent * 100).roundToInt()}%",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
