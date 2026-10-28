package com.example.melora.presentation.folders

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melora.domain.model.AudioFolder
import com.example.melora.domain.model.Song
import com.example.melora.domain.usecase.GetFoldersUseCase
import com.example.melora.domain.usecase.GetSongsByFolderUseCase
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
data class FoldersUiState(
    val folders: List<AudioFolder> = emptyList(),
    val isGridView: Boolean = true,
    val selectedFolder: AudioFolder? = null,
    val selectedFolderSongs: List<Song> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

/**
 * ViewModel for managing audio folders and folder songs reactive state using Clean Architecture UseCases.
 * Preserves state across configuration changes (rotation) without re-fetching.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class FoldersViewModel @Inject constructor(
    getFoldersUseCase: GetFoldersUseCase,
    private val getSongsByFolderUseCase: GetSongsByFolderUseCase,
    private val playSongUseCase: PlaySongUseCase,
    private val playQueueUseCase: PlayQueueUseCase
) : ViewModel() {

    private val _selectedFolder = MutableStateFlow<AudioFolder?>(null)
    private val _isGridView = MutableStateFlow(true)
    private val _isLoading = MutableStateFlow(false)
    private val _errorMessage = MutableStateFlow<String?>(null)

    private val foldersFlow: StateFlow<List<AudioFolder>> = getFoldersUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val selectedFolderSongsFlow: StateFlow<List<Song>> = _selectedFolder
        .flatMapLatest { folder ->
            if (folder == null) flowOf(emptyList())
            else getSongsByFolderUseCase(folder.path)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val uiState: StateFlow<FoldersUiState> = combine(
        foldersFlow,
        _isGridView,
        _selectedFolder,
        selectedFolderSongsFlow,
        _isLoading,
        _errorMessage
    ) { params: Array<Any?> ->
        @Suppress("UNCHECKED_CAST")
        val allFolders = params[0] as List<AudioFolder>
        val isGrid = params[1] as Boolean
        val selected = params[2] as AudioFolder?
        @Suppress("UNCHECKED_CAST")
        val songs = params[3] as List<Song>
        val loading = params[4] as Boolean
        val error = params[5] as String?

        FoldersUiState(
            folders = allFolders,
            isGridView = isGrid,
            selectedFolder = selected,
            selectedFolderSongs = songs,
            isLoading = loading,
            errorMessage = error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = FoldersUiState()
    )

    fun toggleViewMode() {
        _isGridView.value = !_isGridView.value
    }

    fun selectFolder(folder: AudioFolder) {
        _selectedFolder.value = folder
    }

    fun clearSelectedFolder() {
        _selectedFolder.value = null
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
