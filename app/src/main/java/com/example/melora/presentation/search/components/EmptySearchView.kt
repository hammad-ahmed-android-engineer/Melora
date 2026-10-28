package com.example.melora.presentation.search.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.melora.R
import com.example.melora.core.designsystem.AppTypography

@Composable
fun EmptySearchView(
    query: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (query.isBlank()) {
            AppTypography.BodyMedium(
                text = stringResource(R.string.search_prompt),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            AppTypography.BodyMedium(
                text = stringResource(R.string.no_results_found, query)
            )
        }
    }
}
