package com.example.melora.presentation.favorites.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.melora.R
import com.example.melora.core.designsystem.AppTypography

import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesTopBar(favoritesCount: Int) {
    TopAppBar(
        title = {
            AppTypography.TitleLarge(
                text = stringResource(R.string.favorites_header, favoritesCount),
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
    )
}
