package com.example.pelys.domain.model

data class Movie(
    val id: Int,
    val title: String,
    val posterUrl: String?,
    val backdropUrl: String?,
    val voteAverage: Double,
    val releaseDate: String?,
    val overview: String,
    val isFavorite: Boolean
)