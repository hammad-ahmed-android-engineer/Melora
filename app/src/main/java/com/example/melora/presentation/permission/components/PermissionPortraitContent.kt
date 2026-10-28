package com.example.melora.presentation.permission.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import com.example.melora.core.designsystem.components.MeloraText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.melora.R
import com.example.melora.core.designsystem.AppDimensions

@Composable
fun PermissionPortraitContent(
    onRequestPermission: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = AppDimensions.paddingLarge, vertical = AppDimensions.paddingLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(AppDimensions.paddingDefault))

            // Hero Badge
            PermissionHeroBadge()

            Spacer(modifier = Modifier.height(AppDimensions.paddingLarge))

            // Welcome Chip
            PermissionWelcomeChip()

            Spacer(modifier = Modifier.height(AppDimensions.paddingDefault))

            // Headline
            MeloraText(
                text = stringResource(R.string.permission_screen_title),
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp
                ),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))

            // Subtitle
            MeloraText(
                text = stringResource(R.string.permission_screen_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp,
                modifier = Modifier.padding(horizontal = AppDimensions.paddingSmall)
            )

            Spacer(modifier = Modifier.height(AppDimensions.paddingLarge))

            // Feature Benefit Cards
            PermissionFeatureCards()
        }

        Spacer(modifier = Modifier.height(AppDimensions.paddingLarge))

        // CTA Buttons
        PermissionActionButtons(
            onRequestPermission = onRequestPermission,
            onOpenSettings = onOpenSettings
        )
    }
}
