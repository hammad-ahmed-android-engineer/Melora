package com.example.melora.domain.usecase

import com.example.melora.domain.model.AudioFolder
import com.example.melora.domain.model.Song
import com.example.melora.domain.repository.MusicRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFoldersUseCase @Inject constructor(
    private val musicRepository: MusicRepository
) {
    operator fun invoke(): Flow<List<AudioFolder>> = musicRepository.getFolders()
}

class GetSongsByFolderUseCase @Inject constructor(
    private val musicRepository: MusicRepository
) {
    operator fun invoke(folderPath: String): Flow<List<Song>> = musicRepository.getSongsByFolder(folderPath)
}

class GetSongsByAlbumUseCase @Inject constructor(
    private val musicRepository: MusicRepository
) {
    operator fun invoke(albumId: Long): Flow<List<Song>> = musicRepository.getSongsByAlbum(albumId)
}

class GetSongsByArtistUseCase @Inject constructor(
    private val musicRepository: MusicRepository
) {
    operator fun invoke(artistId: Long): Flow<List<Song>> = musicRepository.getSongsByArtist(artistId)
}

class RefreshLibraryUseCase @Inject constructor(
    private val musicRepository: MusicRepository
) {
    suspend operator fun invoke() = musicRepository.refreshLibrary()
}
