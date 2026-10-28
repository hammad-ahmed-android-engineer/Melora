package com.example.melora.presentation.songs

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melora.core.utils.sortSongs
import com.example.melora.domain.model.Playlist
import com.example.melora.domain.model.Song
import com.example.melora.domain.model.SortOption
import com.example.melora.domain.usecase.AddSongToPlaylistUseCase
import com.example.melora.domain.usecase.DeleteSongUseCase
import com.example.melora.domain.usecase.GetAllPlaylistsUseCase
import com.example.melora.domain.usecase.GetAllSongsUseCase
import com.example.melora.domain.usecase.PlaySongUseCase
import com.example.melora.domain.usecase.QueueManagementUseCase
import com.example.melora.domain.usecase.RefreshLibraryUseCase
import com.example.melora.domain.usecase.RenameSongUseCase
import com.example.melora.domain.usecase.SetShuffleUseCase
import com.example.melora.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class SongFilterTab {
    ALL_SONGS,
    FAVORITES
}

@Immutable
data class SongsUiState(
    val songs: List<Song> = emptyList(),
    val filteredSongs: List<Song> = emptyList(),
    val recentlyPlayedTopSongs: List<Song> = emptyList(),
    val playlists: List<Playlist> = emptyList(),
    val sortOption: SortOption = SortOption(),
    val selectedTab: SongFilterTab = SongFilterTab.ALL_SONGS,
    val searchQuery: String = "",
    val selectedSongIds: Set<Long> = emptySet(),
    val isMultiSelectMode: Boolean = false,
    val isAllSelected: Boolean = false,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

@HiltViewModel
class SongsViewModel @Inject constructor(
    getAllSongsUseCase: GetAllSongsUseCase,
    getAllPlaylistsUseCase: GetAllPlaylistsUseCase,
    private val refreshLibraryUseCase: RefreshLibraryUseCase,
    private val playSongUseCase: PlaySongUseCase,
    private val setShuffleUseCase: SetShuffleUseCase,
    private val queueManagementUseCase: QueueManagementUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val addSongToPlaylistUseCase: AddSongToPlaylistUseCase,
    private val deleteSongUseCase: DeleteSongUseCase,
    private val renameSongUseCase: RenameSongUseCase
) : ViewModel() {

    private val _sortOption = MutableStateFlow(SortOption())
    private val _selectedTab = MutableStateFlow(SongFilterTab.ALL_SONGS)
    private val _searchQuery = MutableStateFlow("")
    private val _selectedSongIds = MutableStateFlow<Set<Long>>(emptySet())
    private val _isMultiSelectMode = MutableStateFlow(false)
    private val _isLoading = MutableStateFlow(false)
    private val _errorMessage = MutableStateFlow<String?>(null)

    val uiState: StateFlow<SongsUiState> = combine(
        getAllSongsUseCase(),
        getAllPlaylistsUseCase(),
        _sortOption,
        _selectedTab,
        _searchQuery,
        _selectedSongIds,
        _isMultiSelectMode,
        _isLoading,
        _errorMessage
    ) { params: Array<Any?> ->
        @Suppress("UNCHECKED_CAST")
        val allSongs = params[0] as List<Song>
        @Suppress("UNCHECKED_CAST")
        val playlists = params[1] as List<Playlist>
        val sort = params[2] as SortOption
        val tab = params[3] as SongFilterTab
        val query = params[4] as String
        @Suppress("UNCHECKED_CAST")
        val selectedIds = params[5] as Set<Long>
        val isMultiSelect = params[6] as Boolean
        val error = params[8] as String?

        // Apply Tab Filter
        val tabFiltered = when (tab) {
            SongFilterTab.ALL_SONGS -> allSongs
            SongFilterTab.FAVORITES -> allSongs.filter { it.isFavorite }
        }

        // Apply Search Filter (title, artist, album, filename)
        val searchFiltered = if (query.isBlank()) {
            tabFiltered
        } else {
            val q = query.trim().lowercase()
            tabFiltered.filter { song ->
                song.title.lowercase().contains(q) ||
                        song.artist.lowercase().contains(q) ||
                        song.album.lowercase().contains(q) ||
                        (song.filePath?.lowercase()?.contains(q) == true)
            }
        }

        val sorted = searchFiltered.sortSongs(sort)
        val allIds = sorted.map { it.id }.toSet()
        val isAll = allIds.isNotEmpty() && selectedIds.containsAll(allIds)

        val topRecentlyPlayed = if (tab == SongFilterTab.ALL_SONGS && query.isBlank()) {
            allSongs.filter { it.lastPlayed > 0L }.sortedByDescending { it.lastPlayed }.take(3)
        } else {
            emptyList()
        }

        SongsUiState(
            songs = allSongs,
            filteredSongs = sorted,
            recentlyPlayedTopSongs = topRecentlyPlayed,
            playlists = playlists,
            sortOption = sort,
            selectedTab = tab,
            searchQuery = query,
            selectedSongIds = selectedIds,
            isMultiSelectMode = isMultiSelect,
            isAllSelected = isAll,
            isLoading = false,
            errorMessage = error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SongsUiState()
    )

    fun selectTab(tab: SongFilterTab) {
        _selectedTab.value = tab
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateSortOption(option: SortOption) {
        _sortOption.value = option
    }

    fun playSong(song: Song, queue: List<Song>) {
        playSongUseCase(song, queue)
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

    fun playNext(song: Song) {
        queueManagementUseCase.playNext(song)
    }

    fun addToQueue(song: Song) {
        queueManagementUseCase.addToQueue(song)
    }

    fun shuffleAll() {
        val songs = uiState.value.filteredSongs
        if (songs.isNotEmpty()) {
            setShuffleUseCase(true)
            val randomSong = songs.random()
            playSongUseCase(randomSong, songs.shuffled())
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

    fun deleteSong(song: Song) {
        viewModelScope.launch {
            try {
                deleteSongUseCase(song)
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage
            }
        }
    }

    fun deleteSelectedSongs(songs: List<Song>) {
        viewModelScope.launch {
            try {
                songs.forEach { deleteSongUseCase(it) }
                cancelSelection()
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage
            }
        }
    }

    fun renameSong(song: Song, newTitle: String) {
        viewModelScope.launch {
            try {
                renameSongUseCase(song, newTitle)
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

    // Multi-selection operations
    fun toggleSelectSong(songId: Long) {
        val current = _selectedSongIds.value.toMutableSet()
        if (current.contains(songId)) {
            current.remove(songId)
        } else {
            current.add(songId)
        }
        _selectedSongIds.value = current
        _isMultiSelectMode.value = current.isNotEmpty()
    }

    fun toggleSelectAll() {
        val allIds = uiState.value.filteredSongs.map { it.id }.toSet()
        if (uiState.value.isAllSelected) {
            _selectedSongIds.value = emptySet()
            _isMultiSelectMode.value = false
        } else {
            _selectedSongIds.value = allIds
            _isMultiSelectMode.value = true
        }
    }

    fun clearSelection() {
        _selectedSongIds.value = emptySet()
        _isMultiSelectMode.value = false
    }

    fun cancelSelection() {
        _selectedSongIds.value = emptySet()
        _isMultiSelectMode.value = false
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
