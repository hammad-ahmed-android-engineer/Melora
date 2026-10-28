package com.example.melora.di

import com.example.melora.data.repository.EqualizerRepositoryImpl
import com.example.melora.data.repository.FavoritesRepositoryImpl
import com.example.melora.data.repository.MusicRepositoryImpl
import com.example.melora.data.repository.PlaybackRepositoryImpl
import com.example.melora.data.repository.PlaylistRepositoryImpl
import com.example.melora.data.repository.SettingsRepositoryImpl
import com.example.melora.data.repository.SleepTimerRepositoryImpl
import com.example.melora.domain.repository.EqualizerRepository
import com.example.melora.domain.repository.FavoritesRepository
import com.example.melora.domain.repository.MusicRepository
import com.example.melora.domain.repository.PlaybackRepository
import com.example.melora.domain.repository.PlaylistRepository
import com.example.melora.domain.repository.SettingsRepository
import com.example.melora.domain.repository.SleepTimerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMusicRepository(
        impl: MusicRepositoryImpl
    ): MusicRepository

    @Binds
    @Singleton
    abstract fun bindPlaylistRepository(
        impl: PlaylistRepositoryImpl
    ): PlaylistRepository

    @Binds
    @Singleton
    abstract fun bindFavoritesRepository(
        impl: FavoritesRepositoryImpl
    ): FavoritesRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        impl: SettingsRepositoryImpl
    ): SettingsRepository

    @Binds
    @Singleton
    abstract fun bindPlaybackRepository(
        impl: PlaybackRepositoryImpl
    ): PlaybackRepository

    @Binds
    @Singleton
    abstract fun bindEqualizerRepository(
        impl: EqualizerRepositoryImpl
    ): EqualizerRepository

    @Binds
    @Singleton
    abstract fun bindSleepTimerRepository(
        impl: SleepTimerRepositoryImpl
    ): SleepTimerRepository
}
