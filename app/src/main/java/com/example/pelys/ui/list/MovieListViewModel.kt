package com.example.pelys.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.pelys.data.repository.MovieRepository
import com.example.pelys.domain.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    repository: MovieRepository
) : ViewModel() {

    val movies: Flow<PagingData<Movie>> = repository
        .getPopularMovies()
        .cachedIn(viewModelScope)
}