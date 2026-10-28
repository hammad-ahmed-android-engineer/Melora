package com.example.melora.di

import android.content.Context
import androidx.room.Room
import com.example.melora.data.local.db.MeloraDatabase
import com.example.melora.data.local.db.dao.FavoriteDao
import com.example.melora.data.local.db.dao.HistoryDao
import com.example.melora.data.local.db.dao.PlaylistDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMeloraDatabase(
        @ApplicationContext context: Context
    ): MeloraDatabase {
        return Room.databaseBuilder(
            context,
            MeloraDatabase::class.java,
            "melora.db"
        ).fallbackToDestructiveMigration(dropAllTables = true).build()
    }

    @Provides
    fun providePlaylistDao(db: MeloraDatabase): PlaylistDao = db.playlistDao()

    @Provides
    fun provideFavoriteDao(db: MeloraDatabase): FavoriteDao = db.favoriteDao()

    @Provides
    fun provideHistoryDao(db: MeloraDatabase): HistoryDao = db.historyDao()
}
