package com.example.melora.presentation.equalizer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import com.example.melora.core.designsystem.components.MeloraSlider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.melora.domain.model.EqualizerBand
import com.example.melora.core.designsystem.AppDimensions
import com.example.melora.core.designsystem.AppTypography
import com.example.melora.core.designsystem.dimensions

/**
 * Modern vertical studio console fader slider for equalizer bands.
 * Features illuminated decibel pill badge at the top, vertical fader track, and center frequency readout at the bottom.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EqualizerVerticalBandItem(
    band: EqualizerBand,
    minLevelMb: Short,
    maxLevelMb: Short,
    isEnabled: Boolean,
    onLevelChanged: (Short) -> Unit,
    modifier: Modifier = Modifier
) {
    val dbValue = band.currentLevelMb / 100

    Column(
        modifier = modifier
            .padding(horizontal = AppDimensions.paddingSmall),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Gain readout pill
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(AppDimensions.cornerSmall))
                .background(
                    if (isEnabled && dbValue != 0) MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.surfaceVariant
                )
                .padding(horizontal = 6.dp, vertical = MaterialTheme.dimensions.spacing.micro),
            contentAlignment = Alignment.Center
        ) {
            AppTypography.LabelSmall(
                text = (if (dbValue > 0) "+$dbValue" else "$dbValue") + " dB",
                color = if (isEnabled && dbValue != 0) MaterialTheme.colorScheme.onPrimaryContainer
                else MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))

        // Vertical fader track
        Box(
            modifier = Modifier
                .width(48.dp)
                .height(210.dp),
            contentAlignment = Alignment.Center
        ) {
            MeloraSlider(
                value = band.currentLevelMb.toFloat(),
                onValueChange = { onLevelChanged(it.toInt().toShort()) },
                valueRange = minLevelMb.toFloat()..maxLevelMb.toFloat(),
                enabled = isEnabled,
                modifier = Modifier
                    .width(200.dp)
                    .rotate(-90f)
            )
        }

        Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))

        // Center frequency label at bottom
        AppTypography.LabelSmall(
            text = band.formattedFreq,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold
        )
    }
}
