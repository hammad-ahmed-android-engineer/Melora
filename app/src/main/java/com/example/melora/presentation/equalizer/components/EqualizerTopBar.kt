package com.example.melora.presentation.equalizer.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import com.example.melora.core.designsystem.components.MeloraIconButton
import androidx.compose.material3.TopAppBar
import com.example.melora.core.designsystem.components.MeloraSwitch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.melora.R
import com.example.melora.core.designsystem.AppDimensions
import com.example.melora.core.designsystem.AppTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EqualizerTopBar(
    isEnabled: Boolean,
    isAvailable: Boolean,
    onBackClick: () -> Unit,
    onToggleEnabled: (Boolean) -> Unit
) {
    TopAppBar(
        title = { AppTypography.HeadingMedium(text = stringResource(R.string.audio_equalizer)) },
        navigationIcon = {
            MeloraIconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
        },
        actions = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = AppDimensions.paddingSmall)
            ) {
                AppTypography.LabelSmall(
                    text = stringResource(if (isEnabled) R.string.status_on else R.string.status_off),
                    modifier = Modifier.padding(end = AppDimensions.paddingSmall)
                )
                MeloraSwitch(
                    checked = isEnabled,
                    onCheckedChange = onToggleEnabled,
                    enabled = isAvailable
                )
            }
        }
    )
}
