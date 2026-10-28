package com.example.melora.presentation.permission

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import com.example.melora.core.utils.isLandscape
import com.example.melora.presentation.permission.components.PermissionLandscapeContent
import com.example.melora.presentation.permission.components.PermissionPortraitContent

/**
 * Modern onboarding screen presented when audio storage permission is required.
 */
@Composable
fun PermissionScreen(
    onRequestPermission: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.isLandscape

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.14f),
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.background
                    )
                )
            )
    ) {
        if (isLandscape) {
            PermissionLandscapeContent(
                onRequestPermission = onRequestPermission,
                onOpenSettings = onOpenSettings
            )
        } else {
            PermissionPortraitContent(
                onRequestPermission = onRequestPermission,
                onOpenSettings = onOpenSettings
            )
        }
    }
}
