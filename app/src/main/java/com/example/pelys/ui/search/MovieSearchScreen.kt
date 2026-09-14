package com.example.pelys.ui.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieSearchScreen(
    onMovieClick: (Int) -> Unit,
    viewModel: MovieSearchViewModel = hiltViewModel()
) {
    val query by viewModel.query.collectAsState()
    val results = viewModel.results.collectAsLazyPagingItems()

    Column(Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = query,
            onValueChange = viewModel::onQueryChange,
            label = { Text("Buscar película") },
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        )

        if (query.isBlank()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Escribe algo para buscar")
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(count = results.itemCount, key = results.itemKey { it.id }) { index ->
                    val movie = results[index]
                    if (movie != null) {
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
        }
    }
}