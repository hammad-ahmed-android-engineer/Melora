package com.example.melora.core.designsystem.components.bottomsheets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.melora.R
import com.example.melora.core.designsystem.AppDimensions
import com.example.melora.core.designsystem.AppTypography
import com.example.melora.core.designsystem.components.MeloraBottomSheet
import com.example.melora.core.designsystem.components.MeloraButton
import com.example.melora.core.designsystem.components.MeloraIconButton
import com.example.melora.core.designsystem.components.MeloraTextButton
import kotlin.math.roundToInt

/**
 * Custom bottom sheet for controlling playback speed with gauge ruler and minus/plus controls matching Image 1.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaybackSpeedBottomSheet(
    currentSpeed: Float,
    onSpeedSelected: (Float) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
    var speedState by remember(currentSpeed) { mutableFloatStateOf(currentSpeed) }

    MeloraBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppDimensions.paddingLarge)
                .padding(bottom = AppDimensions.paddingLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppTypography.TitleLarge(
                text = stringResource(R.string.playback_speed),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = AppDimensions.paddingMedium)
            )

            // Top Controls Row: [-]  1.00 X (Reset)  [+]
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppDimensions.paddingSmall),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Minus Button
                MeloraIconButton(
                    onClick = {
                        speedState = ((speedState - 0.05f) * 20).roundToInt() / 20f.coerceIn(0.25f, 2.50f)
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.size(42.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Decrease Speed",
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Center Speed Readout & Reset Icon
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val formattedSpeed = String.format(java.util.Locale.US, "%.2f X", speedState)
                    AppTypography.HeadingLarge(
                        text = formattedSpeed,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp
                    )

                    // Small Circular Reset Icon Button
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        MeloraIconButton(
                            onClick = { speedState = 1.00f },
                            modifier = Modifier.size(26.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Reset Speed",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                // Plus Button
                MeloraIconButton(
                    onClick = {
                        speedState = ((speedState + 0.05f) * 20).roundToInt() / 20f.coerceIn(0.25f, 2.50f)
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.size(42.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Increase Speed",
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(AppDimensions.paddingLarge))

            // Touchable/Draggable Ruler Gauge Canvas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .pointerInput(Unit) {
                        detectDragGestures { change, _ ->
                            val fraction = (change.position.x / size.width).coerceIn(0f, 1f)
                            speedState = (0.25f + fraction * (2.50f - 0.25f) * 20).roundToInt() / 20f
                        }
                    }
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            val fraction = (offset.x / size.width).coerceIn(0f, 1f)
                            speedState = (0.25f + fraction * (2.50f - 0.25f) * 20).roundToInt() / 20f
                        }
                    },
                contentAlignment = Alignment.TopCenter
            ) {
                SpeedRulerGauge(
                    speed = speedState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(65.dp)
                        .padding(horizontal = 12.dp)
                )
            }

            Spacer(modifier = Modifier.height(AppDimensions.paddingMedium))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                MeloraTextButton(
                    text = stringResource(R.string.cancel),
                    onClick = onDismiss
                )
                Spacer(modifier = Modifier.width(AppDimensions.paddingSmall))
                MeloraButton(
                    text = stringResource(R.string.apply),
                    onClick = {
                        onSpeedSelected(speedState)
                        onDismiss()
                    }
                )
            }
        }
    }
}

@Composable
private fun SpeedRulerGauge(
    speed: Float,
    minSpeed: Float = 0.25f,
    maxSpeed: Float = 2.50f,
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val tickColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
    val textStyle = MaterialTheme.typography.labelSmall.copy(
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontSize = 10.sp,
        fontWeight = FontWeight.Medium
    )
    val textMeasurer = rememberTextMeasurer()

    Canvas(modifier = modifier) {
        val width = size.width
        val totalTicks = 45 // (2.50 - 0.25) / 0.05

        val majorTicks = setOf(0.5f, 1.0f, 1.5f, 2.0f, 2.5f)

        for (i in 0..totalTicks) {
            val tickValue = minSpeed + i * 0.05f
            val fraction = (tickValue - minSpeed) / (maxSpeed - minSpeed)
            val x = fraction * width

            val isMajor = majorTicks.any { kotlin.math.abs(it - tickValue) < 0.01f }
            val isCurrent = kotlin.math.abs(tickValue - speed) < 0.025f

            val tickHeight = if (isMajor) 22.dp.toPx() else 12.dp.toPx()
            val strokeWidth = if (isCurrent) 3.dp.toPx() else if (isMajor) 2.dp.toPx() else 1.dp.toPx()
            val color = if (isCurrent) primaryColor else tickColor

            drawLine(
                color = color,
                start = Offset(x, 0f),
                end = Offset(x, tickHeight),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )

            if (isMajor) {
                val label = String.format(java.util.Locale.US, "%.1f X", tickValue)
                val textLayout = textMeasurer.measure(label, textStyle)
                drawText(
                    textLayoutResult = textLayout,
                    topLeft = Offset(x - textLayout.size.width / 2f, tickHeight + 4.dp.toPx())
                )
            }
        }
    }
}
