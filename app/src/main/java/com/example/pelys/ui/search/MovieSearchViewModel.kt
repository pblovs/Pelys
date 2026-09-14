package com.example.pelys.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.pelys.data.repository.MovieRepository
import com.example.pelys.domain.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MovieSearchViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    val results: Flow<PagingData<Movie>> = _query
        .debounce(400)
        .filter { it.isNotBlank() }
        .distinctUntilChanged()
        .flatMapLatest { q -> repository.searchMovies(q) }
        .cachedIn(viewModelScope)

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
    }
}