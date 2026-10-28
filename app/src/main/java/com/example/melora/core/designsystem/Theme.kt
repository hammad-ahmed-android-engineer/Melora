package com.example.melora.core.designsystem

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val LocalThemeIsDark = compositionLocalOf { false }

private val DarkColorScheme = darkColorScheme(
    primary = ForestMintDark,
    onPrimary = ForestBackground,
    primaryContainer = ForestPrimaryContainerDark,
    onPrimaryContainer = ForestOnPrimaryContainerDark,
    secondary = ForestSecondaryDark,
    onSecondary = ForestBackground,
    secondaryContainer = ForestSecondaryContainerDark,
    onSecondaryContainer = ForestOnSecondaryContainerDark,
    tertiary = ForestTertiaryDark,
    onTertiary = ForestBackground,
    tertiaryContainer = ForestTertiaryContainerDark,
    onTertiaryContainer = ForestOnTertiaryContainerDark,
    background = ForestBackground,
    onBackground = ForestOnBackground,
    surface = ForestSurface,
    onSurface = ForestOnSurface,
    surfaceVariant = ForestSurfaceVariant,
    onSurfaceVariant = ForestOnSurfaceVariant,
    surfaceContainerLow = ForestSurfaceLow,
    surfaceContainer = ForestSurface,
    surfaceContainerHigh = ForestSurfaceHigh,
    surfaceContainerHighest = ForestSurfaceHighest,
    outline = ForestOutline,
    outlineVariant = ForestOutlineVariant,
    error = ForestError
)

private val LightColorScheme = lightColorScheme(
    primary = ForestMintLight,
    onPrimary = ForestLightSurface,
    primaryContainer = ForestPrimaryContainerLight,
    onPrimaryContainer = ForestOnPrimaryContainerLight,
    secondary = ForestSecondaryLight,
    onSecondary = ForestLightSurface,
    secondaryContainer = ForestSecondaryContainerLight,
    onSecondaryContainer = ForestOnSecondaryContainerLight,
    tertiary = ForestTertiaryLight,
    onTertiary = ForestLightSurface,
    tertiaryContainer = ForestTertiaryContainerLight,
    onTertiaryContainer = ForestOnTertiaryContainerLight,
    background = ForestLightBackground,
    onBackground = ForestLightOnBackground,
    surface = ForestLightSurface,
    onSurface = ForestLightOnSurface,
    surfaceVariant = ForestLightSurfaceVariant,
    onSurfaceVariant = ForestLightOnSurfaceVariant,
    surfaceContainerLow = ForestLightSurfaceLow,
    surfaceContainer = ForestLightSurfaceVariant,
    surfaceContainerHigh = ForestLightSurfaceHigh,
    surfaceContainerHighest = ForestLightSurfaceHighest,
    outline = ForestLightOutline,
    outlineVariant = ForestLightOutlineVariant,
    error = ForestError
)

/**
 * Central Melora Theme Manager conforming strictly to Android 16 Edge-to-Edge and Material 3 standards.
 */
@Composable
fun MeloraTheme(
    themeMode: String = "system",
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        "dark" -> true
        "light" -> false
        else -> isSystemInDarkTheme()
    }

    val context = LocalContext.current
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                // In Android 15 & 16, system bars are transparent edge-to-edge
                window.statusBarColor = Color.Transparent.toArgb()
                window.navigationBarColor = Color.Transparent.toArgb()
                val windowInsetsController = WindowCompat.getInsetsController(window, view)
                windowInsetsController.isAppearanceLightStatusBars = !darkTheme
                windowInsetsController.isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    val dimensions = resolveDimensions()
    CompositionLocalProvider(
        LocalThemeIsDark provides darkTheme,
        LocalDimensions provides dimensions
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography.material3Typography(dimensions.typeScale),
            content = content
        )
    }
}
