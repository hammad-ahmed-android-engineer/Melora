package com.example.melora.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.melora.presentation.albums.AlbumsScreen
import com.example.melora.presentation.albums.AlbumsViewModel
import com.example.melora.presentation.artists.ArtistsScreen
import com.example.melora.presentation.artists.ArtistsViewModel
import com.example.melora.presentation.equalizer.EqualizerScreen
import com.example.melora.presentation.equalizer.EqualizerViewModel
import com.example.melora.presentation.favorites.FavoritesScreen
import com.example.melora.presentation.favorites.FavoritesViewModel
import com.example.melora.presentation.folders.FoldersScreen
import com.example.melora.presentation.folders.FoldersViewModel
import com.example.melora.presentation.player.PlayerScreen
import com.example.melora.presentation.player.PlayerViewModel
import com.example.melora.presentation.playlists.PlaylistsScreen
import com.example.melora.presentation.playlists.PlaylistsViewModel
import com.example.melora.presentation.search.SearchScreen
import com.example.melora.presentation.search.SearchViewModel
import com.example.melora.presentation.settings.SettingsScreen
import com.example.melora.presentation.settings.SettingsViewModel
import com.example.melora.presentation.songs.SongFilterTab
import com.example.melora.presentation.songs.SongsScreen
import com.example.melora.presentation.songs.SongsViewModel

@Composable
fun MeloraNavGraph(
    navController: androidx.navigation.NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Songs.route,
        modifier = modifier.fillMaxSize(),
        enterTransition = { androidx.compose.animation.EnterTransition.None },
        exitTransition = { androidx.compose.animation.ExitTransition.None },
        popEnterTransition = { androidx.compose.animation.EnterTransition.None },
        popExitTransition = { androidx.compose.animation.ExitTransition.None }
    ) {
        composable(
            route = Screen.Songs.route,
            arguments = listOf(
                navArgument("tab") {
                    type = NavType.StringType
                    defaultValue = "ALL_SONGS"
                }
            )
        ) { backStackEntry ->
            val tabStr = backStackEntry.arguments?.getString("tab") ?: "ALL_SONGS"
            val viewModel = hiltViewModel<SongsViewModel>()
            LaunchedEffect(tabStr) {
                val targetTab = try {
                    SongFilterTab.valueOf(tabStr)
                } catch (_: Exception) {
                    SongFilterTab.ALL_SONGS
                }
                viewModel.selectTab(targetTab)
            }
            SongsScreen(
                viewModel = viewModel,
                onNavigateToPlayer = { navController.navigate(Screen.Player.route) }
            )
        }
        composable(Screen.Albums.route) {
            val viewModel = hiltViewModel<AlbumsViewModel>()
            AlbumsScreen(
                viewModel = viewModel,
                onNavigateToPlayer = { navController.navigate(Screen.Player.route) }
            )
        }
        composable(Screen.Artists.route) {
            val viewModel = hiltViewModel<ArtistsViewModel>()
            ArtistsScreen(
                viewModel = viewModel,
                onNavigateToPlayer = { navController.navigate(Screen.Player.route) }
            )
        }
        composable(Screen.Folders.route) {
            val viewModel = hiltViewModel<FoldersViewModel>()
            FoldersScreen(
                viewModel = viewModel,
                onNavigateToPlayer = { navController.navigate(Screen.Player.route) }
            )
        }
        composable(Screen.Playlists.route) {
            val viewModel = hiltViewModel<PlaylistsViewModel>()
            PlaylistsScreen(
                viewModel = viewModel,
                onNavigateToPlayer = { navController.navigate(Screen.Player.route) }
            )
        }
        composable(Screen.Favorites.route) {
            val viewModel = hiltViewModel<FavoritesViewModel>()
            FavoritesScreen(
                viewModel = viewModel,
                onNavigateToPlayer = { navController.navigate(Screen.Player.route) }
            )
        }
        composable(Screen.Search.route) {
            val viewModel = hiltViewModel<SearchViewModel>()
            SearchScreen(
                viewModel = viewModel,
                onNavigateToPlayer = { navController.navigate(Screen.Player.route) }
            )
        }
        composable(Screen.Settings.route) {
            val viewModel = hiltViewModel<SettingsViewModel>()
            SettingsScreen(
                viewModel = viewModel,
                onNavigateToEqualizer = { navController.navigate(Screen.Equalizer.route) }
            )
        }
        composable(
            route = Screen.Player.route,
            enterTransition = { androidx.compose.animation.EnterTransition.None },
            exitTransition = { androidx.compose.animation.ExitTransition.None },
            popEnterTransition = { androidx.compose.animation.EnterTransition.None },
            popExitTransition = { androidx.compose.animation.ExitTransition.None }
        ) {
            val viewModel = hiltViewModel<PlayerViewModel>()
            PlayerScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onNavigateToEqualizer = { navController.navigate(Screen.Equalizer.route) }
            )
        }
        composable(Screen.Equalizer.route) {
            val viewModel = hiltViewModel<EqualizerViewModel>()
            EqualizerScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
