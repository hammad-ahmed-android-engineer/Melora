package com.example.melora.presentation.search

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melora.domain.model.Song
import com.example.melora.domain.usecase.PlaySongUseCase
import com.example.melora.domain.usecase.SearchMusicUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@Immutable
data class SearchUiState(
    val query: String = "",
    val results: List<Song> = emptyList(),
    val isSearching: Boolean = false,
    val errorMessage: String? = null
)

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    searchMusicUseCase: SearchMusicUseCase,
    private val playSongUseCase: PlaySongUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _errorMessage = MutableStateFlow<String?>(null)

    private val searchResultsFlow: StateFlow<List<Song>> = _searchQuery
        .debounce(300L)
        .flatMapLatest { query ->
            if (query.isBlank()) flowOf(emptyList())
            else searchMusicUseCase(query)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val uiState: StateFlow<SearchUiState> = combine(
        _searchQuery,
        searchResultsFlow,
        _errorMessage
    ) { query, results, error ->
        SearchUiState(
            query = query,
            results = results,
            isSearching = query.isNotBlank() && results.isEmpty(),
            errorMessage = error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SearchUiState()
    )

    fun onQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun playSong(song: Song, queue: List<Song>) {
        playSongUseCase(song, queue)
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
