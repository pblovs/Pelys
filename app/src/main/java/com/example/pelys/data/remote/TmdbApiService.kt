package com.example.pelys.data.remote

import com.example.pelys.data.remote.dto.MovieDto
import com.example.pelys.data.remote.dto.MoviePageDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApiService {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int
    ): MoviePageDto

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int
    ): MoviePageDto

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(@Path("movie_id") movieId: Int): MovieDto
}