package com.example.melora.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistPlay
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.melora.R

sealed class Screen(
    val route: String,
    @StringRes val titleRes: Int? = null,
    val icon: ImageVector? = null
) {
    object Songs : Screen("songs?tab={tab}", R.string.nav_songs, Icons.Default.MusicNote) {
        const val BASE_ROUTE = "songs"
        fun createRoute(tab: String = "ALL_SONGS") = "songs?tab=$tab"
    }
    object Playlists : Screen("playlists", R.string.nav_playlists, Icons.AutoMirrored.Filled.PlaylistPlay)
    object Folders : Screen("folders", R.string.nav_folders, Icons.Default.Folder)
    object Albums : Screen("albums", R.string.nav_albums, Icons.Default.Album)
    object Artists : Screen("artists", R.string.nav_artists, Icons.Default.Person)
    object Favorites : Screen("favorites", R.string.nav_favorites, Icons.Default.Favorite)
    object Search : Screen("search", R.string.nav_search, Icons.Default.Search)
    object Settings : Screen("settings", R.string.nav_settings, Icons.Default.Settings)
    object Player : Screen("player")
    object Equalizer : Screen("equalizer")

    companion object {
        // Bottom bar tabs: Songs, Playlists, Folders, Albums, Artists, Settings
        val portraitNavScreens = listOf(Songs, Playlists, Folders, Albums, Artists, Settings)

        // Navigation rail screens
        val railNavScreens = listOf(Songs, Playlists, Folders, Albums, Artists, Search, Settings)

        // Backward compatibility
        val bottomNavScreens = portraitNavScreens
    }
}
