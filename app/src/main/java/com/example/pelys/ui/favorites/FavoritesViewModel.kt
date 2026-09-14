package com.example.pelys.ui.favorites

import androidx.lifecycle.ViewModel
import com.example.pelys.data.repository.MovieRepository
import com.example.pelys.domain.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    repository: MovieRepository
) : ViewModel() {

    val favorites: Flow<List<Movie>> = repository.getFavorites()
}