package com.example.melora.core.utils

import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.provider.Settings
import android.widget.Toast
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.example.melora.R
import com.example.melora.domain.model.Song
import java.io.File

/**
 * Extension function to convert a domain [Song] to a Media3 [MediaItem] with full metadata.
 */
fun Song.toMediaItem(): MediaItem {
    val metadataBuilder = MediaMetadata.Builder()
        .setTitle(title)
        .setArtist(artist)
        .setAlbumTitle(album)

    if (!artworkUri.isNullOrBlank()) {
        metadataBuilder.setArtworkUri(artworkUri.toUri())
    }

    return MediaItem.Builder()
        .setMediaId(id.toString())
        .setUri(mediaUri.toUri())
        .setMediaMetadata(metadataBuilder.build())
        .build()
}

/**
 * Extension function on [Context] to share an audio track via Android Intent chooser.
 */
fun Context.shareAudioTrack(song: Song) {
    shareAudioTracks(listOf(song))
}

/**
 * Extension function on [Context] to share multiple audio tracks via Android Intent.ACTION_SEND_MULTIPLE chooser.
 */
fun Context.shareAudioTracks(songs: List<Song>) {
    if (songs.isEmpty()) return
    try {
        val shareIntent = if (songs.size == 1) {
            val song = songs.first()
            Intent(Intent.ACTION_SEND).apply {
                if (song.filePath != null && File(song.filePath).exists()) {
                    type = "audio/*"
                    putExtra(Intent.EXTRA_STREAM, song.mediaUri.toUri())
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                } else {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, "${song.title} - ${song.artist}")
                }
            }
        } else {
            val uris = ArrayList(songs.map { it.mediaUri.toUri() })
            Intent(Intent.ACTION_SEND_MULTIPLE).apply {
                type = "audio/*"
                putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        }
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share)))
    } catch (_: Exception) {
        val textList = songs.joinToString("\n") { "${it.title} - ${it.artist}" }
        Toast.makeText(this, textList, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function on [Context] to set an audio track as the default system ringtone.
 */
fun Context.setAsRingtone(song: Song) {
    try {
        if (!Settings.System.canWrite(this)) {
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
                data = "package:$packageName".toUri()
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
            return
        }

        RingtoneManager.setActualDefaultRingtoneUri(
            this,
            RingtoneManager.TYPE_RINGTONE,
            song.mediaUri.toUri()
        )

        Toast.makeText(
            this,
            getString(R.string.ringtone_set_success),
            Toast.LENGTH_SHORT
        ).show()
    } catch (e: Exception) {
        Toast.makeText(
            this,
            e.localizedMessage ?: "Error setting ringtone",
            Toast.LENGTH_SHORT
        ).show()
    }
}
