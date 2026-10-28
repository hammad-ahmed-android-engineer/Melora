package com.example.melora.presentation.player.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import com.example.melora.core.designsystem.components.MeloraSlider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.melora.core.utils.formatTimeMs
import com.example.melora.core.designsystem.AppTypography

@Composable
fun PlayerProgressSlider(
    currentPos: Long,
    duration: Long,
    isSeeking: Boolean,
    sliderPosition: Float,
    onSeekingChange: (Float) -> Unit,
    onSeekingFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalDuration = duration.coerceAtLeast(1L)
    Column(modifier = modifier.fillMaxWidth()) {
        MeloraSlider(
            value = if (isSeeking) sliderPosition else currentPos.toFloat(),
            onValueChange = onSeekingChange,
            onValueChangeFinished = onSeekingFinished,
            valueRange = 0f..totalDuration.toFloat(),
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AppTypography.LabelSmall(
                text = currentPos.formatTimeMs(),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            AppTypography.LabelSmall(
                text = totalDuration.formatTimeMs(),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
