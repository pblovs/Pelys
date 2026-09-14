package com.example.pelys.ui.favorites

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage

@Composable
fun FavoritesScreen(
    onMovieClick: (Int) -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val favorites by viewModel.favorites.collectAsState(initial = emptyList())

    if (favorites.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Todavía no tienes favoritos")
        }
        return
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(favorites, key = { it.id }) { movie ->
            Card(onClick = { onMovieClick(movie.id) }) {
                Column {
                    AsyncImage(
                        model = movie.posterUrl,
                        contentDescription = movie.title,
                        modifier = Modifier.fillMaxWidth().aspectRatio(2f / 3f)
                    )
                    Text(movie.title, Modifier.padding(8.dp))
                }
            }
        }
    }
}