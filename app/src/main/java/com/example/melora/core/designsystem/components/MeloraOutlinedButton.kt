package com.example.melora.core.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import com.example.melora.core.designsystem.dimensions

@Composable
fun MeloraOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(MaterialTheme.dimensions.corners.medium),
    border: BorderStroke? = null
) {
    MeloraOutlinedButton(
        onClick = onClick,
        modifier = modifier.height(MaterialTheme.dimensions.components.buttonHeight),
        enabled = enabled,
        shape = shape,
        border = border
    ) {
        MeloraText(
            text = text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun MeloraOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(MaterialTheme.dimensions.corners.medium),
    border: BorderStroke? = null,
    content: @Composable RowScope.() -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        border = border,
        content = content
    )
}

