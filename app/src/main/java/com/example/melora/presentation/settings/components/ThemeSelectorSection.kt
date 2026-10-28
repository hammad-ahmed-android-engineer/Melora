package com.example.melora.presentation.settings.components

import androidx.compose.foundation.clickable
import com.example.melora.core.utils.clickableDebounced
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import com.example.melora.core.designsystem.components.MeloraRadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.melora.R
import com.example.melora.core.designsystem.AppDimensions
import com.example.melora.core.designsystem.AppTypography

@Composable
fun ThemeSelectorSection(
    currentTheme: String,
    onThemeSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val themeOptions = listOf(
        "system" to stringResource(R.string.theme_system),
        "light" to stringResource(R.string.theme_light),
        "dark" to stringResource(R.string.theme_dark)
    )

    Column(modifier = modifier) {
        AppTypography.TitleMedium(text = stringResource(R.string.app_theme))
        Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))
        themeOptions.forEach { (key, label) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickableDebounced { onThemeSelect(key) }
                    .padding(vertical = AppDimensions.paddingExtraSmall),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MeloraRadioButton(
                    selected = (currentTheme == key),
                    onClick = { onThemeSelect(key) }
                )
                Spacer(modifier = Modifier.width(AppDimensions.paddingSmall))
                AppTypography.BodyLarge(text = label)
            }
        }
    }
}
