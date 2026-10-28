package com.example.melora.presentation.main

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melora.domain.model.AppPreferences
import com.example.melora.domain.model.PlaybackState
import com.example.melora.domain.usecase.GetAppPreferencesUseCase
import com.example.melora.domain.usecase.GetPlaybackStateUseCase
import com.example.melora.domain.usecase.RefreshLibraryUseCase
import com.example.melora.domain.usecase.SkipNextUseCase
import com.example.melora.domain.usecase.TogglePlayPauseUseCase
import com.example.melora.domain.usecase.UpdatePlaybackHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@Immutable
data class VolumeState(
    val volumePercent: Float = 0.5f,
    val isVisible: Boolean = false
)

@Immutable
data class MainUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val appPreferences: AppPreferences = AppPreferences(),
    val playbackState: PlaybackState = PlaybackState(),
    val volumeState: VolumeState = VolumeState()
)

/**
 * Root ViewModel for MainActivity handling global app preferences,
 * root playback state, volume events, and library indexing.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    getAppPreferencesUseCase: GetAppPreferencesUseCase,
    getPlaybackStateUseCase: GetPlaybackStateUseCase,
    private val togglePlayPauseUseCase: TogglePlayPauseUseCase,
    private val skipNextUseCase: SkipNextUseCase,
    private val refreshLibraryUseCase: RefreshLibraryUseCase,
    private val updatePlaybackHistoryUseCase: UpdatePlaybackHistoryUseCase
) : ViewModel() {

    private val appPreferences: StateFlow<AppPreferences> = getAppPreferencesUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppPreferences()
        )

    private val playbackState: StateFlow<PlaybackState> = getPlaybackStateUseCase()
    private val _errorMessage = MutableStateFlow<String?>(null)
    private val _volumeState = MutableStateFlow(VolumeState())
    val volumeState: StateFlow<VolumeState> = _volumeState.asStateFlow()

    private var volumeHideJob: Job? = null

    val uiState: StateFlow<MainUiState> = combine(
        appPreferences,
        playbackState,
        _errorMessage,
        _volumeState
    ) { prefs, state, error, volState ->
        MainUiState(
            appPreferences = prefs,
            playbackState = state,
            errorMessage = error,
            volumeState = volState
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainUiState()
    )

    fun showVolume(percent: Float) {
        _volumeState.value = VolumeState(
            volumePercent = percent.coerceIn(0f, 1f),
            isVisible = true
        )
        volumeHideJob?.cancel()
        volumeHideJob = viewModelScope.launch {
            delay(2000L)
            _volumeState.value = _volumeState.value.copy(isVisible = false)
        }
    }

    fun dismissVolume() {
        _volumeState.value = _volumeState.value.copy(isVisible = false)
    }

    fun clearError() {
        _errorMessage.value = null
    }

    init {
        viewModelScope.launch {
            playbackState
                .mapNotNull { it.currentSong?.id }
                .distinctUntilChanged()
                .collect { songId ->
                    updatePlaybackHistoryUseCase(songId)
                }
        }
    }

    fun togglePlayPause() = togglePlayPauseUseCase()

    fun skipToNext() = skipNextUseCase()

    fun refreshLibrary() {
        viewModelScope.launch {
            refreshLibraryUseCase()
        }
    }
}
