package com.example.pelys.ui.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage

@Composable
fun MovieListScreen(
    onMovieClick: (Int) -> Unit,
    viewModel: MovieListViewModel = hiltViewModel()
) {
    val movies = viewModel.movies.collectAsLazyPagingItems()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                count = movies.itemCount,
                key = movies.itemKey { it.id }
            ) { index ->
                val movie = movies[index]
                if (movie != null) {
                    MovieCard(
                        title = movie.title,
                        posterUrl = movie.posterUrl,
                        rating = movie.voteAverage,
                        onClick = { onMovieClick(movie.id) }
                    )
                }
            }

            // Indicador de carga al final (scroll infinito)
            movies.loadState.append.let { state ->
                if (state is LoadState.Loading) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Box(Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }

        // Estado de carga inicial
        if (movies.loadState.refresh is LoadState.Loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        // Estado de error inicial
        val refreshError = movies.loadState.refresh as? LoadState.Error
        if (refreshError != null) {
            Column(
                modifier = Modifier.align(Alignment.Center).padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("No se pudo cargar. Comprueba tu conexión.")
                Spacer(Modifier.height(8.dp))
                Button(onClick = { movies.retry() }) {
                    Text("Reintentar")
                }
            }
        }
    }
}

@Composable
private fun MovieCard(
    title: String,
    posterUrl: String?,
    rating: Double,
    onClick: () -> Unit
) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Column {
            AsyncImage(
                model = posterUrl,
                contentDescription = title,
                modifier = Modifier.fillMaxWidth().aspectRatio(2f / 3f)
            )
            Column(Modifier.padding(8.dp)) {
                Text(title, style = MaterialTheme.typography.titleSmall, maxLines = 2)
                Text("★ %.1f".format(rating), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}