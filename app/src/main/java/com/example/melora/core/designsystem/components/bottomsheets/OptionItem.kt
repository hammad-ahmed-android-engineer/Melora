package com.example.melora.core.designsystem.components.bottomsheets

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.melora.core.designsystem.AppDimensions
import com.example.melora.core.designsystem.AppTypography
import com.example.melora.core.utils.clickableDebounced

@Composable
fun OptionItem(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    iconTint: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickableDebounced(onClick = onClick)
            .padding(horizontal = AppDimensions.paddingLarge, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint
        )
        Spacer(modifier = Modifier.width(AppDimensions.paddingMedium))
        AppTypography.BodyLarge(
            text = label,
            color = textColor,
            fontWeight = FontWeight.Medium
        )
    }
}
