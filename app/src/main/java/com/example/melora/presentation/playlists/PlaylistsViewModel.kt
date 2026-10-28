package com.example.melora.presentation.playlists

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melora.domain.model.Playlist
import com.example.melora.domain.model.Song
import com.example.melora.domain.usecase.AddSongToPlaylistUseCase
import com.example.melora.domain.usecase.CreatePlaylistUseCase
import com.example.melora.domain.usecase.DeletePlaylistUseCase
import com.example.melora.domain.usecase.GetAllPlaylistsUseCase
import com.example.melora.domain.usecase.GetAllSongsUseCase
import com.example.melora.domain.usecase.GetPlaylistSongsUseCase
import com.example.melora.domain.usecase.PlayQueueUseCase
import com.example.melora.domain.usecase.PlaySongUseCase
import com.example.melora.domain.usecase.RemoveSongFromPlaylistUseCase
import com.example.melora.domain.usecase.SetShuffleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@Immutable
data class PlaylistsUiState(
    val playlists: List<Playlist> = emptyList(),
    val isGridView: Boolean = true,
    val allLibrarySongs: List<Song> = emptyList(),
    val selectedPlaylist: Playlist? = null,
    val selectedPlaylistSongs: List<Song> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

/**
 * ViewModel for managing playlists and playlist songs reactive state via Clean Architecture UseCases.
 * Preserves state across configuration changes (rotation) without re-fetching.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PlaylistsViewModel @Inject constructor(
    getAllPlaylistsUseCase: GetAllPlaylistsUseCase,
    getAllSongsUseCase: GetAllSongsUseCase,
    private val getPlaylistSongsUseCase: GetPlaylistSongsUseCase,
    private val createPlaylistUseCase: CreatePlaylistUseCase,
    private val deletePlaylistUseCase: DeletePlaylistUseCase,
    private val addSongToPlaylistUseCase: AddSongToPlaylistUseCase,
    private val removeSongFromPlaylistUseCase: RemoveSongFromPlaylistUseCase,
    private val playSongUseCase: PlaySongUseCase,
    private val playQueueUseCase: PlayQueueUseCase,
    private val setShuffleUseCase: SetShuffleUseCase
) : ViewModel() {

    private val _selectedPlaylist = MutableStateFlow<Playlist?>(null)
    private val _isGridView = MutableStateFlow(true)
    private val _isLoading = MutableStateFlow(false)
    private val _errorMessage = MutableStateFlow<String?>(null)

    private val playlistsFlow: StateFlow<List<Playlist>> = getAllPlaylistsUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val allLibrarySongsFlow: StateFlow<List<Song>> = getAllSongsUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val selectedPlaylistSongsFlow: StateFlow<List<Song>> = _selectedPlaylist
        .flatMapLatest { playlist ->
            if (playlist == null) flowOf(emptyList())
            else getPlaylistSongsUseCase(playlist.id)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val uiState: StateFlow<PlaylistsUiState> = combine(
        playlistsFlow,
        _isGridView,
        allLibrarySongsFlow,
        _selectedPlaylist,
        selectedPlaylistSongsFlow,
        _isLoading,
        _errorMessage
    ) { params: Array<Any?> ->
        @Suppress("UNCHECKED_CAST")
        val allPlaylists = params[0] as List<Playlist>
        val isGrid = params[1] as Boolean
        @Suppress("UNCHECKED_CAST")
        val allSongs = params[2] as List<Song>
        val selected = params[3] as Playlist?
        @Suppress("UNCHECKED_CAST")
        val songs = params[4] as List<Song>
        val loading = params[5] as Boolean
        val error = params[6] as String?

        PlaylistsUiState(
            playlists = allPlaylists,
            isGridView = isGrid,
            allLibrarySongs = allSongs,
            selectedPlaylist = selected,
            selectedPlaylistSongs = songs,
            isLoading = loading,
            errorMessage = error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PlaylistsUiState()
    )

    fun toggleViewMode() {
        _isGridView.value = !_isGridView.value
    }

    fun selectPlaylist(playlist: Playlist) {
        _selectedPlaylist.value = playlist
    }

    fun clearSelectedPlaylist() {
        _selectedPlaylist.value = null
    }

    fun createPlaylist(name: String) {
        viewModelScope.launch {
            try {
                createPlaylistUseCase(name)
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage
            }
        }
    }

    fun deletePlaylist(playlistId: Long) {
        viewModelScope.launch {
            try {
                deletePlaylistUseCase(playlistId)
                if (_selectedPlaylist.value?.id == playlistId) {
                    clearSelectedPlaylist()
                }
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage
            }
        }
    }

    fun duplicatePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            try {
                val songs = getPlaylistSongsUseCase(playlist.id).firstOrNull() ?: emptyList()
                val newPlaylistId = createPlaylistUseCase("${playlist.name} (Copy)")
                songs.forEach { song ->
                    addSongToPlaylistUseCase(newPlaylistId, song.id)
                }
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage
            }
        }
    }

    fun addSongToPlaylist(playlistId: Long, songId: Long) {
        viewModelScope.launch {
            try {
                addSongToPlaylistUseCase(playlistId, songId)
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage
            }
        }
    }

    fun removeSongFromPlaylist(playlistId: Long, songId: Long) {
        viewModelScope.launch {
            try {
                removeSongFromPlaylistUseCase(playlistId, songId)
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

    fun shuffleAll(queue: List<Song>) {
        if (queue.isNotEmpty()) {
            setShuffleUseCase(true)
            val random = queue.random()
            playSongUseCase(random, queue.shuffled())
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
