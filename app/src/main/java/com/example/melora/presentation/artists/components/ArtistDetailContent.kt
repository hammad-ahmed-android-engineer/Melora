package com.example.melora.presentation.artists.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import com.example.melora.core.designsystem.components.MeloraButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.melora.R
import com.example.melora.domain.model.Artist
import com.example.melora.domain.model.Song
import com.example.melora.core.designsystem.components.SongRowItem
import com.example.melora.core.designsystem.AppDimensions
import com.example.melora.core.designsystem.AppTypography

@Composable
fun ArtistDetailContent(
    artist: Artist,
    songs: List<Song>,
    onPlayAll: () -> Unit,
    onSongClick: (Song) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppDimensions.paddingMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                AppTypography.TitleLarge(text = artist.name)
                AppTypography.LabelSmall(
                    text = stringResource(R.string.songs_and_albums_format, artist.songCount, artist.albumCount),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))
                MeloraButton(
                    text = stringResource(R.string.play_all),
                    onClick = onPlayAll
                )
            }
        }
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(songs, key = { it.id }) { song ->
                SongRowItem(
                    song = song,
                    onClick = { onSongClick(song) },
                    onToggleFavorite = {},
                    onAddToPlaylist = {}
                )
            }
        }
    }
}
