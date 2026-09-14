package com.example.pelys.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    onBackClick: () -> Unit,
    viewModel: MovieDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        when (val state = uiState) {
            is MovieDetailUiState.Loading -> {
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is MovieDetailUiState.Error -> {
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    Text(state.message)
                }
            }
            is MovieDetailUiState.Success -> {
                val movie = state.movie
                Column(Modifier.fillMaxSize().padding(padding)) {
                    AsyncImage(
                        model = movie.backdropUrl ?: movie.posterUrl,
                        contentDescription = movie.title,
                        modifier = Modifier.fillMaxWidth().height(220.dp)
                    )
                    Column(Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(movie.title, style = MaterialTheme.typography.headlineSmall)
                            IconButton(onClick = { viewModel.toggleFavorite() }) {
                                Icon(
                                    imageVector = if (movie.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "Favorito"
                                )
                            }
                        }
                        Text("★ ${movie.voteAverage}  ·  ${movie.releaseDate ?: "Sin fecha"}")
                        Spacer(Modifier.height(12.dp))
                        Text(movie.overview, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}