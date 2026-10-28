package com.example.melora.presentation.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import com.example.melora.core.designsystem.components.MeloraSwitch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.melora.R
import com.example.melora.core.designsystem.AppDimensions
import com.example.melora.core.designsystem.AppTypography

@Composable
fun PlaybackBehaviorSection(
    resumePreviousSession: Boolean,
    onResumeToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        AppTypography.TitleMedium(text = stringResource(R.string.playback_behavior))
        Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = AppDimensions.paddingExtraSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppTypography.BodyLarge(
                text = stringResource(R.string.resume_previous_session),
                modifier = Modifier.weight(1f)
            )
            MeloraSwitch(
                checked = resumePreviousSession,
                onCheckedChange = onResumeToggle
            )
        }
    }
}
