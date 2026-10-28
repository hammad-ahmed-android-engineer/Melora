package com.example.melora.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.melora.domain.model.AppPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class PreferencesDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val DEFAULT_TAB = stringPreferencesKey("default_tab")
        val RESUME_PREVIOUS_SESSION = booleanPreferencesKey("resume_previous_session")
        val REMEMBER_POSITION = booleanPreferencesKey("remember_position")
        val LAST_SONG_ID = longPreferencesKey("last_song_id")
        val LAST_POSITION = longPreferencesKey("last_position")
    }

    val appPreferencesFlow: Flow<AppPreferences> = context.dataStore.data.map { prefs ->
        AppPreferences(
            themeMode = prefs[Keys.THEME_MODE] ?: "system",
            defaultTab = prefs[Keys.DEFAULT_TAB] ?: "home",
            resumePreviousSession = prefs[Keys.RESUME_PREVIOUS_SESSION] ?: true,
            rememberPosition = prefs[Keys.REMEMBER_POSITION] ?: true,
            lastSongId = prefs[Keys.LAST_SONG_ID],
            lastPosition = prefs[Keys.LAST_POSITION] ?: 0L
        )
    }

    suspend fun updatePreferences(update: (AppPreferences) -> AppPreferences) {
        context.dataStore.edit { prefs ->
            val current = AppPreferences(
                themeMode = prefs[Keys.THEME_MODE] ?: "system",
                defaultTab = prefs[Keys.DEFAULT_TAB] ?: "home",
                resumePreviousSession = prefs[Keys.RESUME_PREVIOUS_SESSION] ?: true,
                rememberPosition = prefs[Keys.REMEMBER_POSITION] ?: true,
                lastSongId = prefs[Keys.LAST_SONG_ID],
                lastPosition = prefs[Keys.LAST_POSITION] ?: 0L
            )
            val updated = update(current)
            prefs[Keys.THEME_MODE] = updated.themeMode
            prefs[Keys.DEFAULT_TAB] = updated.defaultTab
            prefs[Keys.RESUME_PREVIOUS_SESSION] = updated.resumePreviousSession
            prefs[Keys.REMEMBER_POSITION] = updated.rememberPosition
            if (updated.lastSongId != null) {
                prefs[Keys.LAST_SONG_ID] = updated.lastSongId
            } else {
                prefs.remove(Keys.LAST_SONG_ID)
            }
            prefs[Keys.LAST_POSITION] = updated.lastPosition
        }
    }
}
