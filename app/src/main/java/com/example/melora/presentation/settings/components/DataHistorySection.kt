package com.example.melora.presentation.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import com.example.melora.core.designsystem.components.MeloraTextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.melora.R
import com.example.melora.core.designsystem.AppDimensions
import com.example.melora.core.designsystem.AppTypography

@Composable
fun DataHistorySection(
    onClearHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        AppTypography.TitleMedium(text = stringResource(R.string.data_history))
        Spacer(modifier = Modifier.height(AppDimensions.paddingExtraSmall))
        MeloraTextButton(
            text = stringResource(R.string.clear_playback_history),
            onClick = onClearHistory
        )
    }
}
