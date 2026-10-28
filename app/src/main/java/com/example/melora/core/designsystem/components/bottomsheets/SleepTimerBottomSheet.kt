package com.example.melora.core.designsystem.components.bottomsheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.melora.R
import com.example.melora.core.designsystem.AppDimensions
import com.example.melora.core.designsystem.AppTypography
import com.example.melora.core.designsystem.components.MeloraBottomSheet
import com.example.melora.core.designsystem.components.MeloraButton
import com.example.melora.core.designsystem.components.MeloraIconButton
import com.example.melora.core.designsystem.components.MeloraSlider
import com.example.melora.core.designsystem.components.MeloraTextButton
import com.example.melora.core.utils.clickableDebounced
import com.example.melora.core.utils.formatTimeSeconds
import kotlin.math.roundToInt

/**
 * Custom Sleep Timer bottom sheet layout matching Image 2 with clean list options and custom minute picker.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SleepTimerBottomSheet(
    isTimerActive: Boolean,
    remainingSeconds: Long,
    onStartTimer: (Int) -> Unit,
    onCancelTimer: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
    var showCustomPicker by remember { mutableStateOf(false) }
    var customMinutes by remember { mutableIntStateOf(30) }

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
            // Header: < Timer & Turn off action
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = AppDimensions.paddingMedium),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)
                ) {
                    MeloraIconButton(
                        onClick = {
                            if (showCustomPicker) {
                                showCustomPicker = false
                            } else {
                                onDismiss()
                            }
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    AppTypography.TitleMedium(
                        text = stringResource(R.string.sleep_timer),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Turn off link button
                MeloraTextButton(
                    text = if (isTimerActive) "${stringResource(R.string.timer_turn_off)} (${remainingSeconds.formatTimeSeconds()})" else stringResource(R.string.timer_turn_off),
                    onClick = {
                        onCancelTimer()
                        onDismiss()
                    },
                    textColor = if (isTimerActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (showCustomPicker) {
                // Custom Duration Picker View
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AppTypography.HeadingLarge(
                        text = stringResource(R.string.minutes_format, customMinutes),
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(AppDimensions.paddingMedium))

                    MeloraSlider(
                        value = customMinutes.toFloat(),
                        onValueChange = { customMinutes = it.roundToInt().coerceIn(1, 120) },
                        valueRange = 1f..120f,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(AppDimensions.paddingMedium))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        MeloraTextButton(
                            text = stringResource(R.string.cancel),
                            onClick = { showCustomPicker = false }
                        )
                        Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))
                        MeloraButton(
                            text = stringResource(R.string.apply),
                            onClick = {
                                onStartTimer(customMinutes)
                                onDismiss()
                            }
                        )
                    }
                }
            } else {
                // List Options matching Image 2
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    TimerOptionRow(
                        label = stringResource(R.string.timer_stop_after_track),
                        onClick = {
                            onStartTimer(0)
                            onDismiss()
                        }
                    )

                    TimerOptionRow(
                        label = stringResource(R.string.timer_15_min),
                        onClick = {
                            onStartTimer(15)
                            onDismiss()
                        }
                    )

                    TimerOptionRow(
                        label = stringResource(R.string.timer_30_min),
                        onClick = {
                            onStartTimer(30)
                            onDismiss()
                        }
                    )

                    TimerOptionRow(
                        label = stringResource(R.string.timer_45_min),
                        onClick = {
                            onStartTimer(45)
                            onDismiss()
                        }
                    )

                    TimerOptionRow(
                        label = stringResource(R.string.timer_60_min),
                        onClick = {
                            onStartTimer(60)
                            onDismiss()
                        }
                    )

                    TimerOptionRow(
                        label = stringResource(R.string.timer_custom),
                        onClick = {
                            showCustomPicker = true
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun TimerOptionRow(
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickableDebounced { onClick() }
            .padding(vertical = 12.dp, horizontal = AppDimensions.paddingSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppTypography.BodyLarge(
            text = label,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
