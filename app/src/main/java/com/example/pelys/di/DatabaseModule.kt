package com.example.pelys.di

import android.content.Context
import androidx.room.Room
import com.example.pelys.data.local.MovieDao
import com.example.pelys.data.local.MovieDatabase
import com.example.pelys.data.local.RemoteKeysDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideMovieDatabase(@ApplicationContext context: Context): MovieDatabase =
        Room.databaseBuilder(context, MovieDatabase::class.java, "movie_database")
            .build()

    @Provides
    fun provideMovieDao(database: MovieDatabase): MovieDao = database.movieDao()

    @Provides
    fun provideRemoteKeysDao(database: MovieDatabase): RemoteKeysDao = database.remoteKeysDao()
}