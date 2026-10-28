package com.example.melora.presentation.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.melora.core.utils.isLandscape
import com.example.melora.presentation.search.components.EmptySearchView
import com.example.melora.presentation.search.components.SearchInputField
import com.example.melora.presentation.search.components.SearchResultsContent
import com.example.melora.presentation.search.components.SearchTopBar

/**
 * Search screen composable for filtering music library by title, artist, or album
 * with landscape adaptive design using segregated components.
 */
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onNavigateToPlayer: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val query = uiState.query
    val results = uiState.results
    val configuration = LocalConfiguration.current
    val landscape = configuration.isLandscape

    Scaffold(
        topBar = { SearchTopBar() }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            SearchInputField(
                query = query,
                onQueryChange = { viewModel.onQueryChange(it) }
            )

            Box(modifier = Modifier.fillMaxSize()) {
                if (query.isBlank() || results.isEmpty()) {
                    EmptySearchView(query = query)
                } else {
                    SearchResultsContent(
                        results = results,
                        isLandscape = landscape,
                        onSongClick = { song ->
                            viewModel.playSong(song, results)
                            onNavigateToPlayer()
                        }
                    )
                }
            }
        }
    }
}
