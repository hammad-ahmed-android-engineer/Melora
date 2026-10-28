package com.example.melora.core.utils

import com.example.melora.domain.model.Song
import com.example.melora.domain.model.SortDirection
import com.example.melora.domain.model.SortField
import com.example.melora.domain.model.SortOption

/**
 * Domain model extension properties and functions.
 */
val Song.formattedDuration: String
    get() = duration.formatTimeMs()

val Song.formattedSize: String
    get() = fileSize.formatFileSize()

val Song.formattedArtistAndDuration: String
    get() = "$artist \u2022 $formattedDuration"

val Song.formattedAlbumAndDuration: String
    get() = "$album \u2022 $formattedDuration"

fun List<Song>.sortSongs(sortOption: SortOption): List<Song> {
    val sorted = when (sortOption.field) {
        SortField.TITLE -> sortedBy { it.title.lowercase() }
        SortField.ARTIST -> sortedBy { it.artist.lowercase() }
        SortField.ALBUM -> sortedBy { it.album.lowercase() }
        SortField.DURATION -> sortedBy { it.duration }
        SortField.DATE_ADDED -> sortedBy { it.dateAdded }
        SortField.TRACK_NUMBER -> sortedBy { it.trackNumber }
    }
    return if (sortOption.direction == SortDirection.DESCENDING) sorted.reversed() else sorted
}
