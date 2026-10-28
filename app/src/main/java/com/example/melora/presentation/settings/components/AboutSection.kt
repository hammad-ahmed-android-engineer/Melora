package com.example.melora.presentation.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.melora.R
import com.example.melora.core.designsystem.AppDimensions
import com.example.melora.core.designsystem.AppTypography

@Composable
fun AboutSection(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        AppTypography.TitleMedium(text = stringResource(R.string.about))
        Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))
        AppTypography.BodyMedium(text = stringResource(R.string.app_version))
        Spacer(modifier = Modifier.height(AppDimensions.paddingTiny))
        AppTypography.BodySmall(
            text = stringResource(R.string.target_api),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
