package com.example.melora.presentation.albums.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import com.example.melora.core.designsystem.components.MeloraIconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.melora.R
import com.example.melora.core.designsystem.AppTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumDetailTopBar(
    albumTitle: String,
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = { AppTypography.TitleLarge(text = albumTitle) },
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
