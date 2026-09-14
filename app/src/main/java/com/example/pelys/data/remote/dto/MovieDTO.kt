package com.example.pelys.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MoviePageDto(
    val page: Int,
    val results: List<MovieDto>,
    @SerialName("total_pages") val totalPages: Int
)

@Serializable
data class MovieDto(
    val id: Int,
    val title: String,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("backdrop_path") val backdropPath: String?,
    @SerialName("vote_average") val voteAverage: Double,
    @SerialName("release_date") val releaseDate: String?,
    val overview: String
)