package com.example.melora.core.designsystem.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.melora.core.designsystem.dimensions

@Composable
fun MeloraButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    )
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(MaterialTheme.dimensions.components.buttonHeight),
        enabled = enabled,
        shape = RoundedCornerShape(MaterialTheme.dimensions.corners.medium),
        colors = colors,
        contentPadding = PaddingValues(horizontal = MaterialTheme.dimensions.spacing.medium)
    ) {
        MeloraText(
            text = text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}
