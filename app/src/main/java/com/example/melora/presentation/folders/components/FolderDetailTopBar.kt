package com.example.melora.presentation.folders.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.melora.R
import com.example.melora.core.designsystem.AppTypography
import com.example.melora.core.designsystem.components.MeloraIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolderDetailTopBar(
    folderName: String,
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = { AppTypography.TitleLarge(text = folderName) },
        navigationIcon = {
            MeloraIconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
        }
    )
}
