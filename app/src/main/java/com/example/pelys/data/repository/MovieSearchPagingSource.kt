package com.example.pelys.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.pelys.data.mapper.toDomain
import com.example.pelys.data.remote.TmdbApiService
import com.example.pelys.domain.model.Movie
import retrofit2.HttpException
import java.io.IOException

class MovieSearchPagingSource(
    private val api: TmdbApiService,
    private val query: String
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: 1
        return try {
            val response = api.searchMovies(query = query, page = page)
            val movies = response.results.map { it.toDomain() }

            LoadResult.Page(
                data = movies,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (page >= response.totalPages) null else page + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}