package com.example.pelys.data.local

import androidx.paging.PagingSource
import androidx.room.*
import com.example.pelys.data.local.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies ORDER BY page ASC")
    fun pagingSource(): PagingSource<Int, MovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<MovieEntity>)

    @Query("DELETE FROM movies")
    suspend fun clearAll()

    @Query("SELECT * FROM movies WHERE isFavorite = 1")
    fun getFavorites(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE id = :movieId")
    suspend fun getMovieById(movieId: Int): MovieEntity?

    @Query("UPDATE movies SET isFavorite = :isFavorite WHERE id = :movieId")
    suspend fun setFavorite(movieId: Int, isFavorite: Boolean)
}