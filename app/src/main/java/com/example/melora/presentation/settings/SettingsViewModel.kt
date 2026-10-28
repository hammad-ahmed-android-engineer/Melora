package com.example.melora.presentation.settings

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melora.domain.model.AppPreferences
import com.example.melora.domain.usecase.ClearPlaybackHistoryUseCase
import com.example.melora.domain.usecase.GetAppPreferencesUseCase
import com.example.melora.domain.usecase.UpdateAppPreferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@Immutable
data class SettingsUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val appPreferences: AppPreferences = AppPreferences()
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    getAppPreferencesUseCase: GetAppPreferencesUseCase,
    private val updateAppPreferencesUseCase: UpdateAppPreferencesUseCase,
    private val clearPlaybackHistoryUseCase: ClearPlaybackHistoryUseCase,
    private val refreshLibraryUseCase: com.example.melora.domain.usecase.RefreshLibraryUseCase
) : ViewModel() {

    private val appPreferences: StateFlow<AppPreferences> = getAppPreferencesUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AppPreferences()
    )

    private val _errorMessage = MutableStateFlow<String?>(null)

    val uiState: StateFlow<SettingsUiState> = combine(
        appPreferences,
        _errorMessage
    ) { prefs, error ->
        SettingsUiState(
            appPreferences = prefs,
            errorMessage = error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsUiState()
    )

    fun clearError() {
        _errorMessage.value = null
    }

    fun updateThemeMode(mode: String) {
        viewModelScope.launch {
            updateAppPreferencesUseCase { it.copy(themeMode = mode) }
        }
    }

    fun updateDefaultTab(tab: String) {
        viewModelScope.launch {
            updateAppPreferencesUseCase { it.copy(defaultTab = tab) }
        }
    }

    fun toggleResumePreviousSession(resume: Boolean) {
        viewModelScope.launch {
            updateAppPreferencesUseCase { it.copy(resumePreviousSession = resume) }
        }
    }

    fun clearPlaybackHistory() {
        viewModelScope.launch {
            clearPlaybackHistoryUseCase()
        }
    }

    fun refreshLibrary() {
        viewModelScope.launch {
            try {
                refreshLibraryUseCase()
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage
            }
        }
    }
}
