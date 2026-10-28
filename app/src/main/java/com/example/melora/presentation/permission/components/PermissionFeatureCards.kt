package com.example.melora.presentation.permission.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.melora.R
import com.example.melora.core.designsystem.AppDimensions

@Composable
fun PermissionFeatureCards(
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall),
        modifier = modifier.fillMaxWidth()
    ) {
        PermissionFeatureCardItem(
            icon = Icons.Default.PlayArrow,
            title = stringResource(R.string.permission_feature_local_title),
            description = stringResource(R.string.permission_feature_local_desc)
        )
        PermissionFeatureCardItem(
            icon = Icons.Default.Folder,
            title = stringResource(R.string.permission_feature_library_title),
            description = stringResource(R.string.permission_feature_library_desc)
        )
        PermissionFeatureCardItem(
            icon = Icons.Default.Lock,
            title = stringResource(R.string.permission_feature_privacy_title),
            description = stringResource(R.string.permission_feature_privacy_desc)
        )
    }
}
