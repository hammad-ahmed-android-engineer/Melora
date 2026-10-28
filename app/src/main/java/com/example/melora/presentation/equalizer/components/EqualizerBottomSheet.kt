package com.example.melora.presentation.equalizer.components

import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.melora.R
import com.example.melora.core.designsystem.AppDimensions
import com.example.melora.core.designsystem.AppTypography
import com.example.melora.core.designsystem.components.MeloraBottomSheet
import com.example.melora.core.designsystem.components.MeloraButton
import com.example.melora.core.designsystem.components.MeloraCard
import com.example.melora.core.designsystem.components.MeloraSwitch
import com.example.melora.presentation.equalizer.EqualizerViewModel

/**
 * Modern audio equalizer bottom sheet enabling real-time frequency band adjustments,
 * preset switching, and DSP effects without navigating away from the player.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EqualizerBottomSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EqualizerViewModel = hiltViewModel(),
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MeloraBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(bottom = AppDimensions.paddingLarge)
        ) {
            // Header: Equalizer Title & Power Switch
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppDimensions.paddingLarge, vertical = AppDimensions.paddingSmall),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)
                ) {
                    Icon(
                        imageVector = Icons.Default.Equalizer,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    AppTypography.HeadingMedium(text = stringResource(R.string.audio_equalizer))
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)
                ) {
                    AppTypography.LabelSmall(
                        text = stringResource(if (uiState.isEnabled) R.string.status_on else R.string.status_off)
                    )
                    MeloraSwitch(
                        checked = uiState.isEnabled,
                        onCheckedChange = { viewModel.toggleEnabled(it) },
                        enabled = uiState.isAvailable
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = AppDimensions.paddingSmall),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )

            if (!uiState.isAvailable) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(AppDimensions.paddingLarge),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)
                ) {
                    Icon(
                        imageVector = Icons.Default.Equalizer,
                        contentDescription = null,
                        modifier = Modifier.size(AppDimensions.artworkSmall),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    AppTypography.TitleMedium(
                        text = stringResource(R.string.eq_not_available),
                        textAlign = TextAlign.Center
                    )
                    MeloraButton(
                        text = stringResource(R.string.refresh),
                        onClick = { viewModel.refreshState() }
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppDimensions.paddingLarge)
                ) {
                    // Presets
                    EqualizerPresetList(
                        presets = uiState.presets,
                        currentPreset = uiState.currentPreset,
                        isEnabled = uiState.isEnabled,
                        onPresetSelect = { viewModel.applyPreset(it) }
                    )

                    Spacer(modifier = Modifier.height(AppDimensions.paddingMedium))

                    AppTypography.TitleMedium(
                        text = stringResource(R.string.frequency_bands),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))

                    // Vertical Faders Card
                    MeloraCard(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState())
                                .padding(vertical = AppDimensions.paddingMedium),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            uiState.bands.forEach { band ->
                                EqualizerVerticalBandItem(
                                    band = band,
                                    minLevelMb = uiState.minLevelMb,
                                    maxLevelMb = uiState.maxLevelMb,
                                    isEnabled = uiState.isEnabled,
                                    onLevelChanged = { newLevel ->
                                        viewModel.setBandLevel(band.bandIndex, newLevel)
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(AppDimensions.paddingMedium))

                    // Audio Effects Card (Bass Boost & Virtualizer)
                    EqualizerEffectsCard(
                        bassBoostStrength = uiState.bassBoostStrength,
                        virtualizerStrength = uiState.virtualizerStrength,
                        isEnabled = uiState.isEnabled,
                        onBassBoostChange = { viewModel.setBassBoost(it) },
                        onVirtualizerChange = { viewModel.setVirtualizer(it) }
                    )
                }
            }
        }
    }
}
