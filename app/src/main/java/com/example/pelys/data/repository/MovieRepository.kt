package com.example.pelys.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.pelys.data.local.MovieDatabase
import com.example.pelys.data.mapper.toDomain
import com.example.pelys.data.remote.TmdbApiService
import com.example.pelys.domain.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val api: TmdbApiService,
    private val db: MovieDatabase
) {
    @OptIn(ExperimentalPagingApi::class)
    fun getPopularMovies(): Flow<PagingData<Movie>> {
        val pagingSourceFactory = { db.movieDao().pagingSource() }

        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            remoteMediator = MovieRemoteMediator(api, db),
            pagingSourceFactory = pagingSourceFactory
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    suspend fun toggleFavorite(movieId: Int, isFavorite: Boolean) {
        db.movieDao().setFavorite(movieId, isFavorite)
    }

    fun getFavorites(): Flow<List<Movie>> =
        db.movieDao().getFavorites().map { list -> list.map { it.toDomain() } }

    suspend fun getMovieDetail(movieId: Int): Movie {
        val local = db.movieDao().getMovieById(movieId)
        if (local != null) return local.toDomain()

        val dto = api.getMovieDetail(movieId)
        val entity = dto.toEntity(page = 0) // página 0 = "no viene de un listado paginado"
        db.movieDao().insertAll(listOf(entity))
        return entity.toDomain()
    }

    fun searchMovies(query: String): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { MovieSearchPagingSource(api, query) }
        ).flow
    }
}