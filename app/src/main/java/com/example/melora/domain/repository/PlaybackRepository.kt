package com.example.melora.domain.repository

import com.example.melora.domain.model.RepeatMode
import com.example.melora.domain.model.PlaybackState
import com.example.melora.domain.model.Song
import kotlinx.coroutines.flow.StateFlow

interface PlaybackRepository {
    val playbackState: StateFlow<PlaybackState>

    fun playSong(song: Song, queue: List<Song> = listOf(song))
    fun playQueue(queue: List<Song>, startIndex: Int = 0)
    fun pause()
    fun resume()
    fun togglePlayPause()
    fun seekTo(positionMs: Long)
    fun skipToNext()
    fun skipToPrevious()
    fun setRepeatMode(repeatMode: RepeatMode)
    fun setShuffleEnabled(enabled: Boolean)
    fun addToQueue(song: Song)
    fun playNext(song: Song)
    fun removeFromQueue(index: Int)
    fun reorderQueue(fromIndex: Int, toIndex: Int)
    fun clearQueue()
    fun setPlaybackSpeed(speed: Float)
}
