package com.example.melora.data.repository

import android.content.ComponentName
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.melora.core.media.EqualizerManager
import com.example.melora.core.media.PlaybackService
import com.example.melora.core.utils.toMediaItem
import com.example.melora.domain.model.PlaybackState
import com.example.melora.domain.model.RepeatMode
import com.example.melora.domain.model.Song
import com.example.melora.domain.repository.PlaybackRepository
import com.example.melora.presentation.widget.MeloraAppWidgetProvider
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaybackRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val equalizerManager: EqualizerManager
) : PlaybackRepository {

    private val handler = Handler(Looper.getMainLooper())
    private var controllerFuture: ListenableFuture<MediaController>? = null
    private var controller: MediaController? = null

    private val _playbackState = MutableStateFlow(PlaybackState())
    override val playbackState: StateFlow<PlaybackState> = _playbackState.asStateFlow()

    private var currentQueueSongs = mutableListOf<Song>()
    private val positionTrackerRunnable = object : Runnable {
        override fun run() {
            if (controller?.isPlaying == true) {
                updateState()
            }
            handler.postDelayed(this, 500L)
        }
    }

    init {
        initController()
    }

    private fun initController() {
        val sessionToken = SessionToken(context, ComponentName(context, PlaybackService::class.java))
        controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture?.addListener({
            try {
                controller = controllerFuture?.get()
                setupControllerListener()
                startPositionTracker()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, MoreExecutors.directExecutor())
    }

    private fun setupControllerListener() {
        controller?.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                updateState()
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                updateState()
                if (playbackState == Player.STATE_READY) {
                    val c = controller
                    if (c != null) {
                        equalizerManager.initAudioSession(c.audioSessionId)
                    }
                }
            }

            override fun onMediaItemTransition(mediaItem: androidx.media3.common.MediaItem?, reason: Int) {
                updateState()
            }

            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                updateState()
            }

            override fun onRepeatModeChanged(repeatMode: Int) {
                updateState()
            }
        })
    }

    private fun startPositionTracker() {
        handler.removeCallbacks(positionTrackerRunnable)
        handler.post(positionTrackerRunnable)
    }

    private fun updateState() {
        val c = controller ?: return
        val currentMediaItem = c.currentMediaItem
        val currentSong = if (currentMediaItem != null) {
            val id = currentMediaItem.mediaId.toLongOrNull()
            currentQueueSongs.find { it.id == id }
        } else null

        val repeat = when (c.repeatMode) {
            Player.REPEAT_MODE_ALL -> RepeatMode.ALL
            Player.REPEAT_MODE_ONE -> RepeatMode.ONE
            else -> RepeatMode.OFF
        }

        val newState = PlaybackState(
            currentSong = currentSong,
            isPlaying = c.isPlaying,
            currentPosition = c.currentPosition.coerceAtLeast(0L),
            duration = c.duration.coerceAtLeast(0L),
            repeatMode = repeat,
            isShuffleEnabled = c.shuffleModeEnabled,
            queue = currentQueueSongs.toList(),
            currentIndex = c.currentMediaItemIndex,
            playbackSpeed = c.playbackParameters.speed
        )
        _playbackState.value = newState
        checkAndUpdateWidget(newState)
    }

    private var lastWidgetSongId: Long? = null
    private var lastWidgetIsPlaying: Boolean? = null

    private fun checkAndUpdateWidget(state: PlaybackState) {
        val songId = state.currentSong?.id
        val isPlaying = state.isPlaying
        if (songId != lastWidgetSongId || isPlaying != lastWidgetIsPlaying) {
            lastWidgetSongId = songId
            lastWidgetIsPlaying = isPlaying
            MeloraAppWidgetProvider.updateAllWidgets(context, state)
        }
    }

    override fun playSong(song: Song, queue: List<Song>) {
        val q = if (queue.isEmpty()) listOf(song) else queue
        playQueue(q, q.indexOf(song).coerceAtLeast(0))
    }

    override fun playQueue(queue: List<Song>, startIndex: Int) {
        val c = controller ?: return
        currentQueueSongs = queue.toMutableList()
        val mediaItems = queue.map { it.toMediaItem() }
        c.setMediaItems(mediaItems, startIndex, 0L)
        c.prepare()
        c.play()
        updateState()
    }

    override fun pause() {
        controller?.pause()
        updateState()
    }

    override fun resume() {
        controller?.play()
        updateState()
    }

    override fun togglePlayPause() {
        val c = controller ?: return
        if (c.isPlaying) {
            c.pause()
        } else {
            c.play()
        }
        updateState()
    }

    override fun seekTo(positionMs: Long) {
        controller?.seekTo(positionMs)
        updateState()
    }

    override fun skipToNext() {
        controller?.seekToNextMediaItem()
        updateState()
    }

    override fun skipToPrevious() {
        controller?.seekToPreviousMediaItem()
        updateState()
    }

    override fun setRepeatMode(repeatMode: RepeatMode) {
        val mode = when (repeatMode) {
            RepeatMode.OFF -> Player.REPEAT_MODE_OFF
            RepeatMode.ALL -> Player.REPEAT_MODE_ALL
            RepeatMode.ONE -> Player.REPEAT_MODE_ONE
        }
        controller?.repeatMode = mode
        updateState()
    }

    override fun setShuffleEnabled(enabled: Boolean) {
        controller?.shuffleModeEnabled = enabled
        updateState()
    }

    override fun addToQueue(song: Song) {
        val c = controller ?: return
        currentQueueSongs.add(song)
        c.addMediaItem(song.toMediaItem())
        updateState()
    }

    override fun playNext(song: Song) {
        val c = controller ?: return
        val nextIndex = (c.currentMediaItemIndex + 1).coerceAtMost(currentQueueSongs.size)
        currentQueueSongs.add(nextIndex, song)
        c.addMediaItem(nextIndex, song.toMediaItem())
        updateState()
    }

    override fun removeFromQueue(index: Int) {
        val c = controller ?: return
        if (index in currentQueueSongs.indices) {
            currentQueueSongs.removeAt(index)
            c.removeMediaItem(index)
            updateState()
        }
    }

    override fun reorderQueue(fromIndex: Int, toIndex: Int) {
        val c = controller ?: return
        if (fromIndex in currentQueueSongs.indices && toIndex in currentQueueSongs.indices) {
            val item = currentQueueSongs.removeAt(fromIndex)
            currentQueueSongs.add(toIndex, item)
            c.moveMediaItem(fromIndex, toIndex)
            updateState()
        }
    }

    override fun clearQueue() {
        val c = controller ?: return
        currentQueueSongs.clear()
        c.clearMediaItems()
        updateState()
    }

    override fun setPlaybackSpeed(speed: Float) {
        val c = controller ?: return
        c.setPlaybackSpeed(speed)
        updateState()
    }
}
