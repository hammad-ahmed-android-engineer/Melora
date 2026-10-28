package com.example.melora.presentation.player.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.melora.core.designsystem.AppDimensions
import com.example.melora.domain.model.PlaybackState
import com.example.melora.domain.model.Song

@Composable
fun PlayerLandscapeLayout(
    song: Song,
    playbackState: PlaybackState,
    artworkScale: Float,
    currentPos: Long,
    duration: Long,
    isSeeking: Boolean,
    sliderPosition: Float,
    innerPadding: PaddingValues,
    queueSize: Int,
    onToggleFavorite: () -> Unit,
    onAddToPlaylist: () -> Unit,
    onSeekingChange: (Float) -> Unit,
    onSeekingFinished: () -> Unit,
    onToggleShuffle: () -> Unit,
    onSkipPrevious: () -> Unit,
    onTogglePlayPause: () -> Unit,
    onSkipNext: () -> Unit,
    onToggleRepeat: () -> Unit,
    onOpenEqualizer: () -> Unit,
    onOpenQueue: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = AppDimensions.paddingLarge, vertical = AppDimensions.paddingSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(0.42f)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            PlayerArtworkSection(
                artworkUri = song.artworkUri,
                scale = artworkScale,
                isLandscape = true,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))
            DjMusicVisualizer(
                isPlaying = playbackState.isPlaying,
                modifier = Modifier.fillMaxWidth(0.92f)
            )
        }

        Spacer(modifier = Modifier.width(AppDimensions.paddingLarge))

        Column(
            modifier = Modifier
                .weight(0.58f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PlayerTrackInfo(
                song = song,
                onToggleFavorite = onToggleFavorite,
                onAddToPlaylist = onAddToPlaylist
            )

            Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))

            PlayerProgressSlider(
                currentPos = currentPos,
                duration = duration,
                isSeeking = isSeeking,
                sliderPosition = sliderPosition,
                onSeekingChange = onSeekingChange,
                onSeekingFinished = onSeekingFinished
            )

            Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))

            PlayerControlsSection(
                isPlaying = playbackState.isPlaying,
                isShuffleEnabled = playbackState.isShuffleEnabled,
                repeatMode = playbackState.repeatMode,
                onToggleShuffle = onToggleShuffle,
                onSkipPrevious = onSkipPrevious,
                onTogglePlayPause = onTogglePlayPause,
                onSkipNext = onSkipNext,
                onToggleRepeat = onToggleRepeat
            )

            Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))

            PlayerBottomToolbar(
                queueSize = queueSize,
                onOpenQueue = onOpenQueue,
                onOpenEqualizer = onOpenEqualizer
            )
        }
    }
}
