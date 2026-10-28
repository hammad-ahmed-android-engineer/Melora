package com.example.melora.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.melora.R
import com.example.melora.core.designsystem.AppDimensions
import com.example.melora.core.designsystem.AppTypography
import com.example.melora.core.designsystem.components.MiniPlayer
import com.example.melora.core.utils.isLandscape
import com.example.melora.domain.model.PlaybackState
import com.example.melora.presentation.permission.PermissionHandler

/**
 * Root navigation host managing screen transitions, adaptive NavigationRail (landscape),
 * bottom navigation bar (portrait), and seamless MiniPlayer docking.
 */
@Composable
fun MeloraNavHost(
    playbackState: PlaybackState,
    onTogglePlayPause: () -> Unit,
    onSkipNext: () -> Unit,
    onPermissionGranted: () -> Unit,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val isPlayerScreen = currentRoute == Screen.Player.route
    val configuration = LocalConfiguration.current
    val landscape = configuration.isLandscape

    PermissionHandler(onPermissionGranted = onPermissionGranted) {
        Row(modifier = modifier.fillMaxSize()) {
            if (landscape && !isPlayerScreen) {
                NavigationRail(
                    header = {
                        Icon(
                            painter = painterResource(R.drawable.ic_splash_logo),
                            contentDescription = stringResource(R.string.app_name),
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .padding(vertical = AppDimensions.paddingSmall)
                                .size(AppDimensions.artworkSmall)
                        )
                    },
                    modifier = Modifier
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState())
                ) {
                    Screen.railNavScreens.forEach { screen ->
                        val screenTitle = screen.titleRes?.let { stringResource(it) }
                        val isSongsRoute = currentRoute?.startsWith(Screen.Songs.BASE_ROUTE) == true
                        val isSelected = when (screen) {
                            is Screen.Songs -> isSongsRoute
                            else -> currentRoute == screen.route
                        }

                        NavigationRailItem(
                            selected = isSelected,
                            onClick = {
                                val targetRoute = if (screen is Screen.Songs) Screen.Songs.createRoute() else screen.route
                                if (currentRoute != targetRoute) {
                                    navController.navigate(targetRoute) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = {
                                screen.icon?.let { icon ->
                                    Icon(imageVector = icon, contentDescription = screenTitle)
                                }
                            },
                            label = { screenTitle?.let { AppTypography.LabelSmall(text = it) } }
                        )
                    }
                }
            }

            Scaffold(
                bottomBar = {
                    if (!landscape && !isPlayerScreen) {
                        Column {
                            if (playbackState.currentSong != null) {
                                MiniPlayer(
                                    playbackState = playbackState,
                                    isLandscape = false,
                                    onClick = { navController.navigate(Screen.Player.route) },
                                    onTogglePlayPause = onTogglePlayPause,
                                    onSkipNext = onSkipNext
                                )
                            }
                            NavigationBar {
                                Screen.portraitNavScreens.forEach { screen ->
                                    val screenTitle = screen.titleRes?.let { stringResource(it) }
                                    val isSongsRoute = currentRoute?.startsWith(Screen.Songs.BASE_ROUTE) == true
                                    val isSelected = when (screen) {
                                        is Screen.Songs -> isSongsRoute
                                        else -> currentRoute == screen.route
                                    }

                                    NavigationBarItem(
                                        selected = isSelected,
                                        onClick = {
                                            val targetRoute = if (screen is Screen.Songs) Screen.Songs.createRoute() else screen.route
                                            if (currentRoute != targetRoute) {
                                                navController.navigate(targetRoute) {
                                                    popUpTo(navController.graph.findStartDestination().id) {
                                                        saveState = true
                                                    }
                                                    launchSingleTop = true
                                                    restoreState = true
                                                }
                                            }
                                        },
                                        icon = {
                                            screen.icon?.let { icon ->
                                                Icon(imageVector = icon, contentDescription = screenTitle)
                                            }
                                        },
                                        label = { screenTitle?.let { AppTypography.LabelSmall(text = it) } }
                                    )
                                }
                            }
                        }
                    }
                },
                contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0, 0, 0, 0),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = innerPadding.calculateBottomPadding())
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        MeloraNavGraph(navController = navController)
                    }

                    if (landscape && !isPlayerScreen && playbackState.currentSong != null) {
                        MiniPlayer(
                            playbackState = playbackState,
                            isLandscape = true,
                            onClick = { navController.navigate(Screen.Player.route) },
                            onTogglePlayPause = onTogglePlayPause,
                            onSkipNext = onSkipNext
                        )
                    }
                }
            }
        }
    }
}
