package com.example.melora.presentation.folders.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.melora.R
import com.example.melora.core.designsystem.AppTypography

@Composable
fun EmptyFoldersView(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AppTypography.BodyLarge(text = stringResource(R.string.no_folders_found))
    }
}
