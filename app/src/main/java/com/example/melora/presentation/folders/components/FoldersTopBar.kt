package com.example.melora.presentation.folders.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.melora.R
import com.example.melora.core.designsystem.AppTypography
import com.example.melora.core.designsystem.components.MeloraIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoldersTopBar(
    foldersCount: Int,
    isGridView: Boolean,
    onToggleView: () -> Unit
) {
    TopAppBar(
        title = {
            AppTypography.TitleLarge(
                text = stringResource(R.string.folders_header, foldersCount),
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        actions = {
            MeloraIconButton(onClick = onToggleView) {
                Icon(
                    imageVector = if (isGridView) Icons.Default.ViewAgenda else Icons.Default.GridView,
                    contentDescription = null
                )
            }
        }
    )
}
