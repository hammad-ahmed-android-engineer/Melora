package com.example.melora.core.designsystem.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun MeloraTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    textColor: Color = MaterialTheme.colorScheme.primary
) {
    MeloraTextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled
    ) {
        MeloraText(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun MeloraTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        content = content
    )
}

