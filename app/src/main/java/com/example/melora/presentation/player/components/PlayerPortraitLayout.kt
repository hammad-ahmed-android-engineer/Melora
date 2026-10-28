package com.example.melora.presentation.player.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.melora.domain.model.PlaybackState
import com.example.melora.domain.model.Song
import com.example.melora.core.designsystem.AppDimensions

@Composable
fun PlayerPortraitLayout(
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = AppDimensions.paddingLarge, vertical = AppDimensions.paddingSmall),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        PlayerArtworkSection(
            artworkUri = song.artworkUri,
            scale = artworkScale,
            isLandscape = false,
            modifier = Modifier
                .weight(1f)
                .padding(vertical = AppDimensions.paddingSmall)
        )

        DjMusicVisualizer(
            isPlaying = playbackState.isPlaying,
            modifier = Modifier.padding(bottom = AppDimensions.paddingSmall)
        )

        Column(modifier = Modifier.fillMaxWidth()) {
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
        }

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

        PlayerBottomToolbar(
            queueSize = queueSize,
            onOpenQueue = onOpenQueue,
            onOpenEqualizer = onOpenEqualizer
        )
    }
}
