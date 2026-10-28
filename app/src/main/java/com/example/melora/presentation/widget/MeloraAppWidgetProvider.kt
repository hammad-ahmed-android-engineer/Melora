package com.example.melora.presentation.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Size
import android.widget.RemoteViews
import com.example.melora.MainActivity
import com.example.melora.R
import com.example.melora.domain.model.PlaybackState
import com.example.melora.domain.repository.PlaybackRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

/**
 * Modern Material 3 Home Screen Widget Provider for Melora Music Player.
 * Delivers real-time playback synchronization and playback controls directly on the home screen.
 */
class MeloraAppWidgetProvider : AppWidgetProvider() {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface WidgetEntryPoint {
        fun playbackRepository(): PlaybackRepository
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val playbackRepo = getPlaybackRepository(context)
        val state = playbackRepo?.playbackState?.value ?: PlaybackState()
        for (appWidgetId in appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId, state)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        val playbackRepo = getPlaybackRepository(context) ?: return

        when (intent.action) {
            ACTION_PLAY_PAUSE -> playbackRepo.togglePlayPause()
            ACTION_SKIP_NEXT -> playbackRepo.skipToNext()
            ACTION_SKIP_PREVIOUS -> playbackRepo.skipToPrevious()
        }
    }

    private fun getPlaybackRepository(context: Context): PlaybackRepository? {
        return try {
            val entryPoint = EntryPointAccessors.fromApplication(
                context.applicationContext,
                WidgetEntryPoint::class.java
            )
            entryPoint.playbackRepository()
        } catch (_: Exception) {
            null
        }
    }

    companion object {
        const val ACTION_PLAY_PAUSE = "com.example.melora.ACTION_WIDGET_PLAY_PAUSE"
        const val ACTION_SKIP_NEXT = "com.example.melora.ACTION_WIDGET_SKIP_NEXT"
        const val ACTION_SKIP_PREVIOUS = "com.example.melora.ACTION_WIDGET_SKIP_PREVIOUS"

        fun updateAllWidgets(context: Context, state: PlaybackState) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val componentName = ComponentName(context, MeloraAppWidgetProvider::class.java)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)
            if (appWidgetIds.isEmpty()) return

            for (appWidgetId in appWidgetIds) {
                updateWidget(context, appWidgetManager, appWidgetId, state)
            }
        }

        fun updateWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int,
            state: PlaybackState
        ) {
            val views = RemoteViews(context.packageName, R.layout.widget_music_player)
            val currentSong = state.currentSong

            // Song Info
            val title = currentSong?.title ?: context.getString(R.string.no_song_playing)
            val artist = currentSong?.artist ?: context.getString(R.string.app_name)
            views.setTextViewText(R.id.widget_song_title, title)
            views.setTextViewText(R.id.widget_song_artist, artist)

            // Play/Pause State
            val playPauseIcon = if (state.isPlaying) {
                R.drawable.ic_widget_pause
            } else {
                R.drawable.ic_widget_play
            }
            views.setImageViewResource(R.id.widget_btn_play_pause, playPauseIcon)

            // Album Art
            val albumArtBitmap = loadAlbumArt(context, currentSong?.artworkUri)
            if (albumArtBitmap != null) {
                views.setImageViewBitmap(R.id.widget_album_art, albumArtBitmap)
            } else {
                views.setImageViewResource(R.id.widget_album_art, R.drawable.ic_widget_music_note)
            }

            // Click Intent to open App
            val appIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            val appPendingIntent = PendingIntent.getActivity(
                context,
                0,
                appIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            views.setOnClickPendingIntent(R.id.widget_root, appPendingIntent)
            views.setOnClickPendingIntent(R.id.widget_album_art, appPendingIntent)
            views.setOnClickPendingIntent(R.id.widget_info_container, appPendingIntent)

            // Previous Button Intent
            val prevIntent = Intent(context, MeloraAppWidgetProvider::class.java).apply {
                action = ACTION_SKIP_PREVIOUS
            }
            val prevPendingIntent = PendingIntent.getBroadcast(
                context,
                1,
                prevIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            views.setOnClickPendingIntent(R.id.widget_btn_previous, prevPendingIntent)

            // Play/Pause Button Intent
            val playPauseIntent = Intent(context, MeloraAppWidgetProvider::class.java).apply {
                action = ACTION_PLAY_PAUSE
            }
            val playPausePendingIntent = PendingIntent.getBroadcast(
                context,
                2,
                playPauseIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            views.setOnClickPendingIntent(R.id.widget_btn_play_pause, playPausePendingIntent)

            // Next Button Intent
            val nextIntent = Intent(context, MeloraAppWidgetProvider::class.java).apply {
                action = ACTION_SKIP_NEXT
            }
            val nextPendingIntent = PendingIntent.getBroadcast(
                context,
                3,
                nextIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            views.setOnClickPendingIntent(R.id.widget_btn_next, nextPendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        private fun loadAlbumArt(context: Context, uriString: String?): Bitmap? {
            if (uriString.isNullOrBlank()) return null
            return try {
                val uri = Uri.parse(uriString)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    context.contentResolver.loadThumbnail(uri, Size(120, 120), null)
                } else {
                    context.contentResolver.openInputStream(uri)?.use { stream ->
                        BitmapFactory.decodeStream(stream)
                    }
                }
            } catch (_: Exception) {
                null
            }
        }
    }
}
