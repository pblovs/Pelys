package com.example.pelys.data.mapper

import com.example.pelys.data.local.entity.MovieEntity
import com.example.pelys.data.remote.dto.MovieDto
import com.example.pelys.domain.model.Movie

private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

fun MovieEntity.toDomain(): Movie = Movie(
    id = id,
    title = title,
    posterUrl = posterPath?.let { "$IMAGE_BASE_URL$it" },
    backdropUrl = backdropPath?.let { "$IMAGE_BASE_URL$it" },
    voteAverage = voteAverage,
    releaseDate = releaseDate,
    overview = overview,
    isFavorite = isFavorite
)

fun MovieDto.toDomain(): Movie = Movie(
    id = id,
    title = title,
    posterUrl = posterPath?.let { "$IMAGE_BASE_URL$it" },
    backdropUrl = backdropPath?.let { "$IMAGE_BASE_URL$it" },
    voteAverage = voteAverage,
    releaseDate = releaseDate,
    overview = overview,
    isFavorite = false
)