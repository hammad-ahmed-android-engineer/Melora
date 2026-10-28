package com.example.melora.presentation.favorites

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melora.domain.model.Song
import com.example.melora.domain.usecase.GetFavoriteSongsUseCase
import com.example.melora.domain.usecase.PlayQueueUseCase
import com.example.melora.domain.usecase.PlaySongUseCase
import com.example.melora.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@Immutable
data class FavoritesUiState(
    val favorites: List<Song> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    getFavoriteSongsUseCase: GetFavoriteSongsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val playSongUseCase: PlaySongUseCase,
    private val playQueueUseCase: PlayQueueUseCase
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    private val _errorMessage = MutableStateFlow<String?>(null)

    private val favoriteSongsFlow: StateFlow<List<Song>> = getFavoriteSongsUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val uiState: StateFlow<FavoritesUiState> = combine(
        favoriteSongsFlow,
        _isLoading,
        _errorMessage
    ) { songs, loading, error ->
        FavoritesUiState(
            favorites = songs,
            isLoading = loading,
            errorMessage = error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = FavoritesUiState()
    )

    fun toggleFavorite(songId: Long) {
        viewModelScope.launch {
            try {
                toggleFavoriteUseCase(songId)
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage
            }
        }
    }

    fun playSong(song: Song, queue: List<Song>) {
        playSongUseCase(song, queue)
    }

    fun playAll(queue: List<Song>) {
        playQueueUseCase(queue, 0)
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
