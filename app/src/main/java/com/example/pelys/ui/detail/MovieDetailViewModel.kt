package com.example.pelys.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pelys.data.repository.MovieRepository
import com.example.pelys.domain.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface MovieDetailUiState {
    object Loading : MovieDetailUiState
    data class Success(val movie: Movie) : MovieDetailUiState
    data class Error(val message: String) : MovieDetailUiState
}

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val repository: MovieRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val movieId: Int = checkNotNull(savedStateHandle["movieId"])

    private val _uiState = MutableStateFlow<MovieDetailUiState>(MovieDetailUiState.Loading)
    val uiState: StateFlow<MovieDetailUiState> = _uiState.asStateFlow()

    init {
        loadMovie()
    }

    private fun loadMovie() {
        viewModelScope.launch {
            _uiState.value = MovieDetailUiState.Loading
            try {
                val movie = repository.getMovieDetail(movieId)
                _uiState.value = MovieDetailUiState.Success(movie)
            } catch (e: Exception) {
                _uiState.value = MovieDetailUiState.Error("No se pudo cargar la película")
            }
        }
    }

    fun toggleFavorite() {
        val current = (_uiState.value as? MovieDetailUiState.Success)?.movie ?: return
        viewModelScope.launch {
            repository.toggleFavorite(current.id, !current.isFavorite)
            _uiState.value = MovieDetailUiState.Success(current.copy(isFavorite = !current.isFavorite))
        }
    }
}