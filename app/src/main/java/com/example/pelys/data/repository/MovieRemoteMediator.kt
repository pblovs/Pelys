package com.example.pelys.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.pelys.data.local.MovieDatabase
import com.example.pelys.data.local.entity.MovieEntity
import com.example.pelys.data.local.entity.RemoteKeys
import com.example.pelys.data.remote.TmdbApiService
import com.example.pelys.data.remote.dto.MovieDto
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val api: TmdbApiService,
    private val db: MovieDatabase
) : RemoteMediator<Int, MovieEntity>() {

    private val movieDao = db.movieDao()
    private val remoteKeysDao = db.remoteKeysDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    val remoteKeys = remoteKeysDao.remoteKeysByMovieId(lastItem.id)
                    val nextPage = remoteKeys?.nextPage
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    nextPage
                }
            }

            val response = api.getPopularMovies(page = page)
            val movies = response.results
            val endOfPaginationReached = movies.isEmpty() || page >= response.totalPages

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    movieDao.clearAll()
                    remoteKeysDao.clearAll()
                }

                val prevPage = if (page == 1) null else page - 1
                val nextPage = if (endOfPaginationReached) null else page + 1

                val keys = movies.map { RemoteKeys(movieId = it.id, prevPage = prevPage, nextPage = nextPage) }
                remoteKeysDao.insertAll(keys)

                val entities = movies.map { it.toEntity(page = page) }
                movieDao.insertAll(entities)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        } catch (e: IOException) {
            MediatorResult.Error(e) // sin conexión
        } catch (e: HttpException) {
            MediatorResult.Error(e) // error del servidor (4xx, 5xx)
        }
    }
}

fun MovieDto.toEntity(page: Int) = MovieEntity(
    id = id,
    title = title,
    posterPath = posterPath,
    backdropPath = backdropPath,
    voteAverage = voteAverage,
    releaseDate = releaseDate,
    overview = overview,
    page = page
)