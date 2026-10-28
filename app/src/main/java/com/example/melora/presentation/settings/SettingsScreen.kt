package com.example.melora.presentation.settings

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.History
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import com.example.melora.core.designsystem.components.MeloraTextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.melora.R
import com.example.melora.core.designsystem.components.MeloraDialog
import com.example.melora.presentation.settings.components.AboutSection
import com.example.melora.presentation.settings.components.DataHistorySection
import com.example.melora.presentation.settings.components.PlaybackBehaviorSection
import com.example.melora.presentation.settings.components.SettingsCategoryItem
import com.example.melora.presentation.settings.components.SettingsTopBar
import com.example.melora.presentation.settings.components.ThemeSelectorSection
import com.example.melora.core.designsystem.AppDimensions
import com.example.melora.core.designsystem.AppTypography

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onNavigateToEqualizer: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val prefs = uiState.appPreferences
    val context = LocalContext.current

    var showThemeDialog by androidx.compose.runtime.saveable.rememberSaveable { mutableStateOf(false) }
    var showPlaybackDialog by androidx.compose.runtime.saveable.rememberSaveable { mutableStateOf(false) }
    var showDataDialog by androidx.compose.runtime.saveable.rememberSaveable { mutableStateOf(false) }
    var showAboutDialog by androidx.compose.runtime.saveable.rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = { SettingsTopBar() }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(bottom = AppDimensions.paddingExtraLarge),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            item {
                SettingsCategoryItem(
                    title = stringResource(R.string.settings_appearance),
                    description = stringResource(R.string.settings_appearance_desc),
                    icon = Icons.Default.Palette,
                    onClick = { showThemeDialog = true }
                )
            }

            item {
                SettingsCategoryItem(
                    title = stringResource(R.string.settings_playback),
                    description = stringResource(R.string.settings_playback_desc),
                    icon = Icons.Default.Tune,
                    onClick = { showPlaybackDialog = true }
                )
            }

            item {
                SettingsCategoryItem(
                    title = stringResource(R.string.settings_audio),
                    description = stringResource(R.string.settings_audio_desc),
                    icon = Icons.Default.GraphicEq,
                    onClick = onNavigateToEqualizer
                )
            }

            item {
                SettingsCategoryItem(
                    title = stringResource(R.string.settings_music_library),
                    description = stringResource(R.string.settings_music_library_desc),
                    icon = Icons.AutoMirrored.Filled.LibraryBooks,
                    onClick = {
                        viewModel.refreshLibrary()
                        Toast.makeText(context, "Scanning library...", Toast.LENGTH_SHORT).show()
                    }
                )
            }

            item {
                SettingsCategoryItem(
                    title = stringResource(R.string.settings_data_history),
                    description = stringResource(R.string.settings_data_history_desc),
                    icon = Icons.Default.History,
                    onClick = { showDataDialog = true }
                )
            }

            item {
                SettingsCategoryItem(
                    title = stringResource(R.string.settings_notifications),
                    description = stringResource(R.string.settings_notifications_desc),
                    icon = Icons.Default.Notifications,
                    onClick = {
                        try {
                            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                            }
                            context.startActivity(intent)
                        } catch (_: Exception) {
                            Toast.makeText(context, "Media notifications are enabled", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }


            item {
                SettingsCategoryItem(
                    title = stringResource(R.string.settings_about),
                    description = stringResource(R.string.settings_about_desc),
                    icon = Icons.Default.Info,
                    onClick = { showAboutDialog = true }
                )
            }

            item {
                Spacer(modifier = Modifier.height(AppDimensions.paddingLarge))
            }
        }
    }

    if (showThemeDialog) {
        MeloraDialog(
            onDismissRequest = { showThemeDialog = false },
            title = stringResource(R.string.settings_appearance)
        ) {
            ThemeSelectorSection(
                currentTheme = prefs.themeMode,
                onThemeSelect = {
                    viewModel.updateThemeMode(it)
                    showThemeDialog = false
                }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                MeloraTextButton(
                    text = stringResource(R.string.close),
                    onClick = { showThemeDialog = false }
                )
            }
        }
    }

    if (showPlaybackDialog) {
        MeloraDialog(
            onDismissRequest = { showPlaybackDialog = false },
            title = stringResource(R.string.settings_playback)
        ) {
            PlaybackBehaviorSection(
                resumePreviousSession = prefs.resumePreviousSession,
                onResumeToggle = { viewModel.toggleResumePreviousSession(it) }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                MeloraTextButton(
                    text = stringResource(R.string.close),
                    onClick = { showPlaybackDialog = false }
                )
            }
        }
    }

    if (showDataDialog) {
        MeloraDialog(
            onDismissRequest = { showDataDialog = false },
            title = stringResource(R.string.settings_data_history)
        ) {
            DataHistorySection(
                onClearHistory = {
                    viewModel.clearPlaybackHistory()
                    showDataDialog = false
                }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                MeloraTextButton(
                    text = stringResource(R.string.close),
                    onClick = { showDataDialog = false }
                )
            }
        }
    }

    if (showAboutDialog) {
        MeloraDialog(
            onDismissRequest = { showAboutDialog = false },
            title = stringResource(R.string.settings_about)
        ) {
            AboutSection()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                MeloraTextButton(
                    text = stringResource(R.string.close),
                    onClick = { showAboutDialog = false }
                )
            }
        }
    }
}
