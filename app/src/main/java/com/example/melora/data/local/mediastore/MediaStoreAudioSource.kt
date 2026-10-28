package com.example.melora.data.local.mediastore

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import androidx.core.net.toUri
import com.example.melora.domain.model.Album
import com.example.melora.domain.model.Artist
import com.example.melora.domain.model.AudioFolder
import com.example.melora.domain.model.Song
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaStoreAudioSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend fun getSongs(): List<Song> = withContext(Dispatchers.IO) {
        val songList = mutableListOf<Song>()
        val collection = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ARTIST_ID,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.TRACK,
            MediaStore.Audio.Media.YEAR,
            MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.IS_MUSIC
        )

        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0 AND ${MediaStore.Audio.Media.DURATION} >= 10000"

        try {
            context.contentResolver.query(
                collection,
                projection,
                selection,
                null,
                "${MediaStore.Audio.Media.TITLE} ASC"
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                val titleNameCol = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
                val artistCol = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
                val albumCol = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)
                val albumIdCol = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
                val artistIdCol = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID)
                val durationCol = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)
                val trackCol = cursor.getColumnIndex(MediaStore.Audio.Media.TRACK)
                val yearCol = cursor.getColumnIndex(MediaStore.Audio.Media.YEAR)
                val sizeCol = cursor.getColumnIndex(MediaStore.Audio.Media.SIZE)
                val dateAddedCol = cursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED)
                val dataCol = cursor.getColumnIndex(MediaStore.Audio.Media.DATA)

                val artworkBaseUri = "content://media/external/audio/albumart".toUri()

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val title = if (titleNameCol != -1) cursor.getString(titleNameCol) ?: "Unknown" else "Unknown"
                    val artist = if (artistCol != -1) cursor.getString(artistCol) ?: "<Unknown>" else "<Unknown>"
                    val album = if (albumCol != -1) cursor.getString(albumCol) ?: "Unknown Album" else "Unknown Album"
                    val albumId = if (albumIdCol != -1) cursor.getLong(albumIdCol) else 0L
                    val artistId = if (artistIdCol != -1) cursor.getLong(artistIdCol) else 0L
                    val duration = if (durationCol != -1) cursor.getLong(durationCol) else 0L
                    val track = if (trackCol != -1) cursor.getInt(trackCol) else 0
                    val year = if (yearCol != -1) cursor.getInt(yearCol) else 0
                    val size = if (sizeCol != -1) cursor.getLong(sizeCol) else 0L
                    val dateAdded = if (dateAddedCol != -1) cursor.getLong(dateAddedCol) else 0L
                    val filePath = if (dataCol != -1) cursor.getString(dataCol) else null

                    val mediaUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id).toString()
                    val artworkUri = if (albumId > 0) ContentUris.withAppendedId(artworkBaseUri, albumId).toString() else null

                    songList.add(
                        Song(
                            id = id,
                            mediaUri = mediaUri,
                            title = title,
                            artist = if (artist == "<unknown>") "Unknown Artist" else artist,
                            album = album,
                            albumId = albumId,
                            artistId = artistId,
                            duration = duration,
                            trackNumber = track % 1000,
                            year = year,
                            artworkUri = artworkUri,
                            filePath = filePath,
                            fileSize = size,
                            dateAdded = dateAdded
                        )
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        songList
    }

    suspend fun getAlbums(): List<Album> = withContext(Dispatchers.IO) {
        val songs = getSongs()
        val artworkBaseUri = "content://media/external/audio/albumart".toUri()
        songs.groupBy { it.albumId }.map { (albumId, albumSongs) ->
            val first = albumSongs.first()
            Album(
                id = albumId,
                title = first.album,
                artist = first.artist,
                songCount = albumSongs.size,
                artworkUri = if (albumId > 0) ContentUris.withAppendedId(artworkBaseUri, albumId).toString() else null,
                year = albumSongs.maxOfOrNull { it.year } ?: 0
            )
        }
    }

    suspend fun getArtists(): List<Artist> = withContext(Dispatchers.IO) {
        val songs = getSongs()
        songs.groupBy { it.artistId }.map { (artistId, artistSongs) ->
            val first = artistSongs.first()
            val albumCount = artistSongs.map { it.albumId }.distinct().size
            Artist(
                id = artistId,
                name = first.artist,
                songCount = artistSongs.size,
                albumCount = albumCount,
                artworkUri = artistSongs.firstOrNull { it.artworkUri != null }?.artworkUri
            )
        }
    }

    suspend fun getFolders(): List<AudioFolder> = withContext(Dispatchers.IO) {
        val songs = getSongs()
        val folderMap = mutableMapOf<String, MutableList<Song>>()

        songs.forEach { song ->
            val path = song.filePath
            if (path != null) {
                val folderPath = File(path).parent ?: "Unknown"
                folderMap.getOrPut(folderPath) { mutableListOf() }.add(song)
            }
        }

        folderMap.map { (path, folderSongs) ->
            val folderName = File(path).name
            AudioFolder(
                path = path,
                name = if (folderName.isBlank()) "Root" else folderName,
                songCount = folderSongs.size
            )
        }
    }

    suspend fun deleteSong(song: Song): Boolean = withContext(Dispatchers.IO) {
        try {
            val uri = song.mediaUri.toUri()
            val rows = context.contentResolver.delete(uri, null, null)
            if (song.filePath != null) {
                val file = File(song.filePath)
                if (file.exists()) {
                    file.delete()
                }
            }
            rows > 0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun renameSong(song: Song, newTitle: String): Boolean = withContext(Dispatchers.IO) {
        if (newTitle.isBlank()) return@withContext false
        try {
            val uri = song.mediaUri.toUri()
            val values = android.content.ContentValues().apply {
                put(MediaStore.Audio.Media.TITLE, newTitle.trim())
            }
            val rows = context.contentResolver.update(uri, values, null, null)
            rows > 0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
