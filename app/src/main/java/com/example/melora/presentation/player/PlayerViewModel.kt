package com.example.melora.presentation.player

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melora.domain.model.PlaybackState
import com.example.melora.domain.model.Playlist
import com.example.melora.domain.model.RepeatMode
import com.example.melora.domain.usecase.AddSongToPlaylistUseCase
import com.example.melora.domain.usecase.CancelSleepTimerUseCase
import com.example.melora.domain.usecase.GetAllPlaylistsUseCase
import com.example.melora.domain.usecase.GetIsSleepTimerActiveUseCase
import com.example.melora.domain.usecase.GetPlaybackStateUseCase
import com.example.melora.domain.usecase.GetSleepTimerRemainingTimeUseCase
import com.example.melora.domain.usecase.PausePlaybackUseCase
import com.example.melora.domain.usecase.QueueManagementUseCase
import com.example.melora.domain.usecase.SeekToUseCase
import com.example.melora.domain.usecase.SetPlaybackSpeedUseCase
import com.example.melora.domain.usecase.SetRepeatModeUseCase
import com.example.melora.domain.usecase.SetShuffleUseCase
import com.example.melora.domain.usecase.SkipNextUseCase
import com.example.melora.domain.usecase.SkipPreviousUseCase
import com.example.melora.domain.usecase.StartSleepTimerUseCase
import com.example.melora.domain.usecase.ToggleFavoriteUseCase
import com.example.melora.domain.usecase.TogglePlayPauseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@Immutable
data class PlayerUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val playbackState: PlaybackState = PlaybackState(),
    val remainingTimeSeconds: Long = 0L,
    val isSleepTimerActive: Boolean = false,
    val playlists: List<Playlist> = emptyList()
)

@HiltViewModel
class PlayerViewModel @Inject constructor(
    getPlaybackStateUseCase: GetPlaybackStateUseCase,
    getAllPlaylistsUseCase: GetAllPlaylistsUseCase,
    getSleepTimerRemainingTimeUseCase: GetSleepTimerRemainingTimeUseCase,
    getIsSleepTimerActiveUseCase: GetIsSleepTimerActiveUseCase,
    private val togglePlayPauseUseCase: TogglePlayPauseUseCase,
    private val pausePlaybackUseCase: PausePlaybackUseCase,
    private val skipNextUseCase: SkipNextUseCase,
    private val skipPreviousUseCase: SkipPreviousUseCase,
    private val seekToUseCase: SeekToUseCase,
    private val setRepeatModeUseCase: SetRepeatModeUseCase,
    private val setShuffleUseCase: SetShuffleUseCase,
    private val setPlaybackSpeedUseCase: SetPlaybackSpeedUseCase,
    private val queueManagementUseCase: QueueManagementUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val addSongToPlaylistUseCase: AddSongToPlaylistUseCase,
    private val startSleepTimerUseCase: StartSleepTimerUseCase,
    private val cancelSleepTimerUseCase: CancelSleepTimerUseCase
) : ViewModel() {

    private val playbackState = getPlaybackStateUseCase()
    private val remainingTimeSeconds = getSleepTimerRemainingTimeUseCase()
    private val isSleepTimerActive = getIsSleepTimerActiveUseCase()
    private val playlists = getAllPlaylistsUseCase()
    private val _errorMessage = MutableStateFlow<String?>(null)

    val uiState: StateFlow<PlayerUiState> = combine(
        playbackState,
        remainingTimeSeconds,
        isSleepTimerActive,
        playlists,
        _errorMessage
    ) { state, remaining, active, playlistList, error ->
        PlayerUiState(
            playbackState = state,
            remainingTimeSeconds = remaining,
            isSleepTimerActive = active,
            playlists = playlistList,
            errorMessage = error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PlayerUiState()
    )

    fun clearError() {
        _errorMessage.value = null
    }

    fun togglePlayPause() = togglePlayPauseUseCase()

    fun skipToNext() = skipNextUseCase()

    fun skipToPrevious() = skipPreviousUseCase()

    fun seekTo(positionMs: Long) = seekToUseCase(positionMs)

    fun rewind10() {
        val current = playbackState.value.currentPosition
        seekToUseCase((current - 10000L).coerceAtLeast(0L))
    }

    fun forward10() {
        val current = playbackState.value.currentPosition
        val duration = playbackState.value.duration
        seekToUseCase((current + 10000L).coerceAtMost(duration))
    }

    fun toggleRepeatMode() {
        val current = playbackState.value.repeatMode
        val next = when (current) {
            RepeatMode.OFF -> RepeatMode.ALL
            RepeatMode.ALL -> RepeatMode.ONE
            RepeatMode.ONE -> RepeatMode.OFF
        }
        setRepeatModeUseCase(next)
    }

    fun toggleShuffle() {
        val current = playbackState.value.isShuffleEnabled
        setShuffleUseCase(!current)
    }

    fun toggleFavorite() {
        val currentSong = playbackState.value.currentSong ?: return
        viewModelScope.launch {
            toggleFavoriteUseCase(currentSong.id)
        }
    }

    fun addSongToPlaylist(playlistId: Long, songId: Long) {
        viewModelScope.launch {
            addSongToPlaylistUseCase(playlistId, songId)
        }
    }

    fun removeFromQueue(index: Int) = queueManagementUseCase.removeFromQueue(index)

    fun clearQueue() = queueManagementUseCase.clearQueue()

    fun startSleepTimer(durationMinutes: Int) {
        startSleepTimerUseCase(durationMinutes) {
            pausePlaybackUseCase()
        }
    }

    fun cancelSleepTimer() = cancelSleepTimerUseCase()

    fun setPlaybackSpeed(speed: Float) = setPlaybackSpeedUseCase(speed)
}
