package com.example.melora.presentation.albums

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melora.domain.model.Album
import com.example.melora.domain.model.Song
import com.example.melora.domain.usecase.GetAlbumsUseCase
import com.example.melora.domain.usecase.GetSongsByAlbumUseCase
import com.example.melora.domain.usecase.PlayQueueUseCase
import com.example.melora.domain.usecase.PlaySongUseCase
import com.example.melora.domain.usecase.QueueManagementUseCase
import com.example.melora.domain.usecase.SetShuffleUseCase
import com.example.melora.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI State for AlbumsScreen maintaining selections across configuration changes / rotations.
 */
@Immutable
data class AlbumsUiState(
    val albums: List<Album> = emptyList(),
    val filteredAlbums: List<Album> = emptyList(),
    val searchQuery: String = "",
    val isGridView: Boolean = true,
    val selectedAlbum: Album? = null,
    val selectedAlbumSongs: List<Song> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

/**
 * ViewModel for managing albums, search filtering, and album details using Clean Architecture UseCases.
 * Retains state through configuration changes and screen rotation.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class AlbumsViewModel @Inject constructor(
    getAlbumsUseCase: GetAlbumsUseCase,
    private val getSongsByAlbumUseCase: GetSongsByAlbumUseCase,
    private val playSongUseCase: PlaySongUseCase,
    private val playQueueUseCase: PlayQueueUseCase,
    private val setShuffleUseCase: SetShuffleUseCase,
    private val queueManagementUseCase: QueueManagementUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _isGridView = MutableStateFlow(true)
    private val _selectedAlbum = MutableStateFlow<Album?>(null)
    private val _isLoading = MutableStateFlow(false)
    private val _errorMessage = MutableStateFlow<String?>(null)

    private val albumsFlow: StateFlow<List<Album>> = combine(
        getAlbumsUseCase(),
        _searchQuery
    ) { allAlbums, query ->
        if (query.isBlank()) {
            allAlbums
        } else {
            val q = query.trim().lowercase()
            allAlbums.filter { it.title.lowercase().contains(q) || it.artist.lowercase().contains(q) }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val selectedAlbumSongsFlow: StateFlow<List<Song>> = _selectedAlbum
        .flatMapLatest { album ->
            if (album == null) flowOf(emptyList())
            else getSongsByAlbumUseCase(album.id)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val uiState: StateFlow<AlbumsUiState> = combine(
        albumsFlow,
        _searchQuery,
        _isGridView,
        _selectedAlbum,
        selectedAlbumSongsFlow,
        _isLoading,
        _errorMessage
    ) { params: Array<Any?> ->
        @Suppress("UNCHECKED_CAST")
        val currentAlbums = params[0] as List<Album>
        val query = params[1] as String
        val isGrid = params[2] as Boolean
        val selected = params[3] as Album?
        @Suppress("UNCHECKED_CAST")
        val songs = params[4] as List<Song>
        val loading = params[5] as Boolean
        val error = params[6] as String?

        AlbumsUiState(
            albums = currentAlbums,
            filteredAlbums = currentAlbums,
            searchQuery = query,
            isGridView = isGrid,
            selectedAlbum = selected,
            selectedAlbumSongs = songs,
            isLoading = loading,
            errorMessage = error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AlbumsUiState()
    )

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleViewMode() {
        _isGridView.value = !_isGridView.value
    }

    fun selectAlbum(album: Album) {
        _selectedAlbum.value = album
    }

    fun clearSelectedAlbum() {
        _selectedAlbum.value = null
    }

    fun playSong(song: Song, queue: List<Song>) {
        playSongUseCase(song, queue)
    }

    fun playAll(queue: List<Song>) {
        playQueueUseCase(queue, 0)
    }

    fun shuffleAll(queue: List<Song>) {
        if (queue.isNotEmpty()) {
            setShuffleUseCase(true)
            val random = queue.random()
            playSongUseCase(random, queue.shuffled())
        }
    }

    fun addToQueue(song: Song) {
        queueManagementUseCase.addToQueue(song)
    }

    fun addAlbumToQueue(songs: List<Song>) {
        songs.forEach { queueManagementUseCase.addToQueue(it) }
    }

    fun toggleFavorite(songId: Long) {
        viewModelScope.launch {
            try {
                toggleFavoriteUseCase(songId)
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
