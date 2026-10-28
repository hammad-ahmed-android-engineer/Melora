package com.example.melora.domain.usecase

import com.example.melora.domain.model.PlaybackState
import com.example.melora.domain.model.RepeatMode
import com.example.melora.domain.model.Song
import com.example.melora.domain.repository.PlaybackRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetPlaybackStateUseCase @Inject constructor(
    private val playbackRepository: PlaybackRepository
) {
    operator fun invoke(): StateFlow<PlaybackState> = playbackRepository.playbackState
}

class PlaySongUseCase @Inject constructor(
    private val playbackRepository: PlaybackRepository
) {
    operator fun invoke(song: Song, queue: List<Song> = listOf(song)) {
        playbackRepository.playSong(song, queue)
    }
}

class PlayQueueUseCase @Inject constructor(
    private val playbackRepository: PlaybackRepository
) {
    operator fun invoke(queue: List<Song>, startIndex: Int = 0) {
        playbackRepository.playQueue(queue, startIndex)
    }
}

class TogglePlayPauseUseCase @Inject constructor(
    private val playbackRepository: PlaybackRepository
) {
    operator fun invoke() {
        playbackRepository.togglePlayPause()
    }
}

class SkipNextUseCase @Inject constructor(
    private val playbackRepository: PlaybackRepository
) {
    operator fun invoke() {
        playbackRepository.skipToNext()
    }
}

class SkipPreviousUseCase @Inject constructor(
    private val playbackRepository: PlaybackRepository
) {
    operator fun invoke() {
        playbackRepository.skipToPrevious()
    }
}

class SeekToUseCase @Inject constructor(
    private val playbackRepository: PlaybackRepository
) {
    operator fun invoke(positionMs: Long) {
        playbackRepository.seekTo(positionMs)
    }
}

class SetShuffleUseCase @Inject constructor(
    private val playbackRepository: PlaybackRepository
) {
    operator fun invoke(enabled: Boolean) {
        playbackRepository.setShuffleEnabled(enabled)
    }
}

class SetRepeatModeUseCase @Inject constructor(
    private val playbackRepository: PlaybackRepository
) {
    operator fun invoke(repeatMode: RepeatMode) {
        playbackRepository.setRepeatMode(repeatMode)
    }
}

class QueueManagementUseCase @Inject constructor(
    private val playbackRepository: PlaybackRepository
) {
    fun addToQueue(song: Song) = playbackRepository.addToQueue(song)
    fun playNext(song: Song) = playbackRepository.playNext(song)
    fun removeFromQueue(index: Int) = playbackRepository.removeFromQueue(index)
    fun clearQueue() = playbackRepository.clearQueue()
    fun reorderQueue(fromIndex: Int, toIndex: Int) = playbackRepository.reorderQueue(fromIndex, toIndex)
}

class PausePlaybackUseCase @Inject constructor(
    private val playbackRepository: PlaybackRepository
) {
    operator fun invoke() {
        playbackRepository.pause()
    }
}

class SetPlaybackSpeedUseCase @Inject constructor(
    private val playbackRepository: PlaybackRepository
) {
    operator fun invoke(speed: Float) {
        playbackRepository.setPlaybackSpeed(speed)
    }
}
