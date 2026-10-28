package com.example.melora.presentation.permission.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.example.melora.R
import com.example.melora.core.designsystem.AppDimensions

@Composable
fun PermissionLandscapeContent(
    onRequestPermission: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(AppDimensions.paddingLarge),
        horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingLarge),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left Column: Hero badge & Title
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = AppDimensions.paddingDefault),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            PermissionHeroBadge()
            Spacer(modifier = Modifier.height(AppDimensions.paddingDefault))
            PermissionWelcomeChip()
            Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))
            MeloraText(
                text = stringResource(R.string.permission_screen_title),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(AppDimensions.paddingExtraSmall))
            MeloraText(
                text = stringResource(R.string.permission_screen_subtitle),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }

        // Right Column: Feature highlights & CTA buttons
        Column(
            modifier = Modifier.weight(1.2f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            PermissionFeatureCards()
            Spacer(modifier = Modifier.height(AppDimensions.paddingDefault))
            PermissionActionButtons(
                onRequestPermission = onRequestPermission,
                onOpenSettings = onOpenSettings
            )
        }
    }
}
