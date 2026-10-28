package com.example.melora.presentation.equalizer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.SurroundSound
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.melora.R
import com.example.melora.core.designsystem.AppDimensions
import com.example.melora.core.designsystem.AppTypography
import com.example.melora.core.designsystem.components.MeloraCard
import com.example.melora.core.designsystem.components.MeloraSlider

/**
 * Modern Equalizer audio effects console card for Bass Boost and 3D Virtualizer.
 */
@Composable
fun EqualizerEffectsCard(
    bassBoostStrength: Int,
    virtualizerStrength: Int,
    isEnabled: Boolean,
    onBassBoostChange: (Int) -> Unit,
    onVirtualizerChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        AppTypography.TitleMedium(
            text = stringResource(R.string.audio_effects),
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))

        MeloraCard(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
            shape = RoundedCornerShape(AppDimensions.cardCornerRadius)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppDimensions.paddingMedium),
                verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
            ) {
                // Bass Boost Block
                val bassPercent = bassBoostStrength / 10
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)
                        ) {
                            Icon(
                                imageVector = Icons.Default.GraphicEq,
                                contentDescription = null,
                                tint = if (isEnabled && bassPercent > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                            AppTypography.BodyMedium(
                                text = stringResource(R.string.bass_boost, bassPercent),
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(AppDimensions.cornerSmall))
                                .background(
                                    if (isEnabled && bassPercent > 0) MaterialTheme.colorScheme.primaryContainer
                                    else MaterialTheme.colorScheme.surfaceVariant
                                )
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            AppTypography.LabelSmall(
                                text = "$bassPercent%",
                                color = if (isEnabled && bassPercent > 0) MaterialTheme.colorScheme.onPrimaryContainer
                                else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    MeloraSlider(
                        value = bassBoostStrength.toFloat(),
                        onValueChange = { onBassBoostChange(it.toInt()) },
                        valueRange = 0f..1000f,
                        enabled = isEnabled,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Virtualizer Block
                val virtPercent = virtualizerStrength / 10
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SurroundSound,
                                contentDescription = null,
                                tint = if (isEnabled && virtPercent > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                            AppTypography.BodyMedium(
                                text = stringResource(R.string.virtualizer, virtPercent),
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(AppDimensions.cornerSmall))
                                .background(
                                    if (isEnabled && virtPercent > 0) MaterialTheme.colorScheme.primaryContainer
                                    else MaterialTheme.colorScheme.surfaceVariant
                                )
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            AppTypography.LabelSmall(
                                text = "$virtPercent%",
                                color = if (isEnabled && virtPercent > 0) MaterialTheme.colorScheme.onPrimaryContainer
                                else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    MeloraSlider(
                        value = virtualizerStrength.toFloat(),
                        onValueChange = { onVirtualizerChange(it.toInt()) },
                        valueRange = 0f..1000f,
                        enabled = isEnabled,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
