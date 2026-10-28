package com.example.melora.presentation.player

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.melora.R
import com.example.melora.core.designsystem.AppTypography
import com.example.melora.core.designsystem.components.bottomsheets.LyricsBottomSheet
import com.example.melora.core.designsystem.components.bottomsheets.PlaybackSpeedBottomSheet
import com.example.melora.core.designsystem.components.bottomsheets.QueueBottomSheet
import com.example.melora.core.designsystem.components.bottomsheets.SelectPlaylistBottomSheet
import com.example.melora.core.designsystem.components.bottomsheets.SleepTimerBottomSheet
import com.example.melora.core.designsystem.components.bottomsheets.SongInfoBottomSheet
import com.example.melora.core.utils.isLandscape
import com.example.melora.presentation.player.components.PlayerLandscapeLayout
import com.example.melora.presentation.player.components.PlayerPortraitLayout
import com.example.melora.presentation.player.components.PlayerTopBar

/**
 * Main full-screen audio player composable delegating to dedicated landscape and portrait layouts.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel,
    onBackClick: () -> Unit = {},
    onNavigateToEqualizer: () -> Unit = {}
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val playbackState = uiState.playbackState
    val playlists = uiState.playlists
    val isTimerActive = uiState.isSleepTimerActive
    val remainingSeconds = uiState.remainingTimeSeconds

    val song = playbackState.currentSong

    var isSeeking by androidx.compose.runtime.saveable.rememberSaveable { mutableStateOf(false) }
    var sliderPosition by androidx.compose.runtime.saveable.rememberSaveable { mutableFloatStateOf(0f) }

    var showQueueSheet by androidx.compose.runtime.saveable.rememberSaveable { mutableStateOf(false) }
    var showEqualizerSheet by androidx.compose.runtime.saveable.rememberSaveable { mutableStateOf(false) }
    var showSleepTimerDialog by androidx.compose.runtime.saveable.rememberSaveable { mutableStateOf(false) }
    var showSongInfoDialog by androidx.compose.runtime.saveable.rememberSaveable { mutableStateOf(false) }
    var showSpeedDialog by androidx.compose.runtime.saveable.rememberSaveable { mutableStateOf(false) }
    var showLyricsDialog by androidx.compose.runtime.saveable.rememberSaveable { mutableStateOf(false) }
    var showSelectPlaylistDialog by androidx.compose.runtime.saveable.rememberSaveable { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState()
    val configuration = LocalConfiguration.current
    val landscape = configuration.isLandscape

    BackHandler(enabled = showQueueSheet || showEqualizerSheet) {
        if (showEqualizerSheet) {
            showEqualizerSheet = false
        } else {
            showQueueSheet = false
        }
    }

    val artworkScale by animateFloatAsState(
        targetValue = if (playbackState.isPlaying) 1.0f else 0.88f,
        animationSpec = spring(),
        label = "ArtworkScale"
    )

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.24f),
            MaterialTheme.colorScheme.secondary.copy(alpha = 0.12f),
            MaterialTheme.colorScheme.background
        )
    )

    Scaffold(
        topBar = {
            PlayerTopBar(
                sourceName = stringResource(R.string.from_your_library),
                onBackClick = onBackClick,
                onOpenSongInfo = { showSongInfoDialog = true },
                onOpenAddToPlaylist = { showSelectPlaylistDialog = true },
                onOpenLyrics = { showLyricsDialog = true },
                onOpenSpeed = { showSpeedDialog = true },
                onOpenSleepTimer = { showSleepTimerDialog = true },
                onOpenEqualizer = { showEqualizerSheet = true },
                onShare = {
                    song?.let {
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, "${it.title} - ${it.artist}")
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, null)
                        context.startActivity(shareIntent)
                    }
                }
            )
        },
        containerColor = Color.Transparent,
        modifier = Modifier.background(backgroundBrush)
    ) { innerPadding ->
        if (song == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                AppTypography.BodyLarge(text = stringResource(R.string.no_song_playing))
            }
        } else {
            val currentPos = if (isSeeking) sliderPosition.toLong() else playbackState.currentPosition
            val duration = playbackState.duration.coerceAtLeast(1L)

            if (landscape) {
                PlayerLandscapeLayout(
                    song = song,
                    playbackState = playbackState,
                    artworkScale = artworkScale,
                    currentPos = currentPos,
                    duration = duration,
                    isSeeking = isSeeking,
                    sliderPosition = sliderPosition,
                    innerPadding = innerPadding,
                    queueSize = playbackState.queue.size,
                    onToggleFavorite = { viewModel.toggleFavorite() },
                    onAddToPlaylist = { showSelectPlaylistDialog = true },
                    onSeekingChange = {
                        isSeeking = true
                        sliderPosition = it
                    },
                    onSeekingFinished = {
                        isSeeking = false
                        viewModel.seekTo(sliderPosition.toLong())
                    },
                    onToggleShuffle = { viewModel.toggleShuffle() },
                    onSkipPrevious = { viewModel.skipToPrevious() },
                    onTogglePlayPause = { viewModel.togglePlayPause() },
                    onSkipNext = { viewModel.skipToNext() },
                    onToggleRepeat = { viewModel.toggleRepeatMode() },
                    onOpenEqualizer = { showEqualizerSheet = true },
                    onOpenQueue = { showQueueSheet = true }
                )
            } else {
                PlayerPortraitLayout(
                    song = song,
                    playbackState = playbackState,
                    artworkScale = artworkScale,
                    currentPos = currentPos,
                    duration = duration,
                    isSeeking = isSeeking,
                    sliderPosition = sliderPosition,
                    innerPadding = innerPadding,
                    queueSize = playbackState.queue.size,
                    onToggleFavorite = { viewModel.toggleFavorite() },
                    onAddToPlaylist = { showSelectPlaylistDialog = true },
                    onSeekingChange = {
                        isSeeking = true
                        sliderPosition = it
                    },
                    onSeekingFinished = {
                        isSeeking = false
                        viewModel.seekTo(sliderPosition.toLong())
                    },
                    onToggleShuffle = { viewModel.toggleShuffle() },
                    onSkipPrevious = { viewModel.skipToPrevious() },
                    onTogglePlayPause = { viewModel.togglePlayPause() },
                    onSkipNext = { viewModel.skipToNext() },
                    onToggleRepeat = { viewModel.toggleRepeatMode() },
                    onOpenEqualizer = { showEqualizerSheet = true },
                    onOpenQueue = { showQueueSheet = true }
                )
            }
        }

        if (showQueueSheet) {
            QueueBottomSheet(
                queue = playbackState.queue,
                currentIndex = playbackState.currentIndex,
                sheetState = sheetState,
                onDismiss = { showQueueSheet = false }
            )
        }

        if (showEqualizerSheet) {
            com.example.melora.presentation.equalizer.components.EqualizerBottomSheet(
                onDismiss = { showEqualizerSheet = false }
            )
        }

        if (showSleepTimerDialog) {
            SleepTimerBottomSheet(
                isTimerActive = isTimerActive,
                remainingSeconds = remainingSeconds,
                onStartTimer = { mins -> viewModel.startSleepTimer(mins) },
                onCancelTimer = { viewModel.cancelSleepTimer() },
                onDismiss = { showSleepTimerDialog = false }
            )
        }

        if (showSongInfoDialog && song != null) {
            SongInfoBottomSheet(
                song = song,
                onDismiss = { showSongInfoDialog = false }
            )
        }

        if (showLyricsDialog && song != null) {
            LyricsBottomSheet(
                song = song,
                onDismiss = { showLyricsDialog = false }
            )
        }

        if (showSpeedDialog) {
            PlaybackSpeedBottomSheet(
                currentSpeed = playbackState.playbackSpeed,
                onSpeedSelected = { speed -> viewModel.setPlaybackSpeed(speed) },
                onDismiss = { showSpeedDialog = false }
            )
        }

        if (showSelectPlaylistDialog && song != null) {
            SelectPlaylistBottomSheet(
                playlists = playlists,
                onPlaylistSelected = { playlistId ->
                    viewModel.addSongToPlaylist(playlistId, song.id)
                    showSelectPlaylistDialog = false
                },
                onDismiss = { showSelectPlaylistDialog = false }
            )
        }
    }
}
