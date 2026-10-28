package com.example.melora.presentation.equalizer.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.melora.R
import com.example.melora.core.designsystem.AppDimensions
import com.example.melora.core.designsystem.AppTypography
import com.example.melora.presentation.songs.components.FilterPill

@Composable
fun EqualizerPresetList(
    presets: List<String>,
    currentPreset: Short,
    isEnabled: Boolean,
    onPresetSelect: (Short) -> Unit,
    modifier: Modifier = Modifier
) {
    if (presets.isNotEmpty()) {
        Column(modifier = modifier.fillMaxWidth()) {
            AppTypography.TitleMedium(text = stringResource(R.string.presets))
            Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)
            ) {
                presets.forEachIndexed { index, name ->
                    FilterPill(
                        text = name,
                        selected = (currentPreset.toInt() == index),
                        onClick = { if (isEnabled) onPresetSelect(index.toShort()) }
                    )
                }
            }
        }
    }
}
