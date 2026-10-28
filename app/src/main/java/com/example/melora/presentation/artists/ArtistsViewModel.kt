package com.example.melora.presentation.artists

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melora.domain.model.Artist
import com.example.melora.domain.model.Song
import com.example.melora.domain.usecase.GetArtistsUseCase
import com.example.melora.domain.usecase.GetSongsByArtistUseCase
import com.example.melora.domain.usecase.PlayQueueUseCase
import com.example.melora.domain.usecase.PlaySongUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@Immutable
data class ArtistsUiState(
    val artists: List<Artist> = emptyList(),
    val isGridView: Boolean = true,
    val selectedArtist: Artist? = null,
    val selectedArtistSongs: List<Song> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

/**
 * ViewModel for managing artists and detail songs reactive state using Clean Architecture UseCases.
 * Preserves state across configuration changes (rotation) without re-fetching.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ArtistsViewModel @Inject constructor(
    getArtistsUseCase: GetArtistsUseCase,
    private val getSongsByArtistUseCase: GetSongsByArtistUseCase,
    private val playSongUseCase: PlaySongUseCase,
    private val playQueueUseCase: PlayQueueUseCase
) : ViewModel() {

    private val _selectedArtist = MutableStateFlow<Artist?>(null)
    private val _isGridView = MutableStateFlow(true)
    private val _isLoading = MutableStateFlow(false)
    private val _errorMessage = MutableStateFlow<String?>(null)

    private val artistsFlow: StateFlow<List<Artist>> = getArtistsUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val selectedArtistSongsFlow: StateFlow<List<Song>> = _selectedArtist
        .flatMapLatest { artist ->
            if (artist == null) flowOf(emptyList())
            else getSongsByArtistUseCase(artist.id)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val uiState: StateFlow<ArtistsUiState> = combine(
        artistsFlow,
        _isGridView,
        _selectedArtist,
        selectedArtistSongsFlow,
        _isLoading,
        _errorMessage
    ) { params: Array<Any?> ->
        @Suppress("UNCHECKED_CAST")
        val allArtists = params[0] as List<Artist>
        val isGrid = params[1] as Boolean
        val selected = params[2] as Artist?
        @Suppress("UNCHECKED_CAST")
        val songs = params[3] as List<Song>
        val loading = params[4] as Boolean
        val error = params[5] as String?

        ArtistsUiState(
            artists = allArtists,
            isGridView = isGrid,
            selectedArtist = selected,
            selectedArtistSongs = songs,
            isLoading = loading,
            errorMessage = error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ArtistsUiState()
    )

    fun toggleViewMode() {
        _isGridView.value = !_isGridView.value
    }

    fun selectArtist(artist: Artist) {
        _selectedArtist.value = artist
    }

    fun clearSelectedArtist() {
        _selectedArtist.value = null
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
