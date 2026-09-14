package com.example.pelys.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pelys.data.local.entity.MovieEntity
import com.example.pelys.data.local.entity.RemoteKeys

@Database(
    entities = [MovieEntity::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}