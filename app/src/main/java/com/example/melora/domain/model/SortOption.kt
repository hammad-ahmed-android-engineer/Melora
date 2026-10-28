package com.example.melora.domain.model

import androidx.compose.runtime.Immutable

enum class SortField {
    TITLE,
    ARTIST,
    ALBUM,
    DURATION,
    DATE_ADDED,
    TRACK_NUMBER
}

enum class SortDirection {
    ASCENDING,
    DESCENDING
}

@Immutable
data class SortOption(
    val field: SortField = SortField.TITLE,
    val direction: SortDirection = SortDirection.ASCENDING
)
