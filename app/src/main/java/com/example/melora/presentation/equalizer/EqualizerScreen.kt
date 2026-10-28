package com.example.melora.presentation.equalizer

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import com.example.melora.core.designsystem.components.MeloraButton
import com.example.melora.core.designsystem.components.MeloraCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.melora.R
import com.example.melora.core.utils.isLandscape
import com.example.melora.presentation.equalizer.components.EqualizerEffectsCard
import com.example.melora.presentation.equalizer.components.EqualizerPresetList
import com.example.melora.presentation.equalizer.components.EqualizerTopBar
import com.example.melora.presentation.equalizer.components.EqualizerVerticalBandItem
import com.example.melora.core.designsystem.AppDimensions
import com.example.melora.core.designsystem.AppTypography

/**
 * Equalizer screen composable for frequency bands adjustment with vertical faders and audio effects.
 */
@Composable
fun EqualizerScreen(
    viewModel: EqualizerViewModel,
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val configuration = LocalConfiguration.current
    val landscape = configuration.isLandscape

    Scaffold(
        topBar = {
            EqualizerTopBar(
                isEnabled = uiState.isEnabled,
                isAvailable = uiState.isAvailable,
                onBackClick = onBackClick,
                onToggleEnabled = { viewModel.toggleEnabled(it) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(AppDimensions.paddingMedium)
        ) {
            if (!uiState.isAvailable) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
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
            } else if (landscape) {
                // Landscape Two-Pane: Presets & Audio Effects Left, Vertical Frequency Faders Right
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingLarge)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(0.45f)
                            .fillMaxHeight()
                            .verticalScroll(rememberScrollState())
                    ) {
                        EqualizerPresetList(
                            presets = uiState.presets,
                            currentPreset = uiState.currentPreset,
                            isEnabled = uiState.isEnabled,
                            onPresetSelect = { viewModel.applyPreset(it) }
                        )

                        Spacer(modifier = Modifier.height(AppDimensions.paddingMedium))

                        EqualizerEffectsCard(
                            bassBoostStrength = uiState.bassBoostStrength,
                            virtualizerStrength = uiState.virtualizerStrength,
                            isEnabled = uiState.isEnabled,
                            onBassBoostChange = { viewModel.setBassBoost(it) },
                            onVirtualizerChange = { viewModel.setVirtualizer(it) }
                        )
                    }

                    Column(
                        modifier = Modifier
                            .weight(0.55f)
                            .fillMaxHeight()
                    ) {
                        AppTypography.TitleMedium(
                            text = stringResource(R.string.frequency_bands),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))

                        MeloraCard(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
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
                    }
                }
            } else {
                // Portrait Layout
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
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

                    // Vertical Fader Console Card
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
