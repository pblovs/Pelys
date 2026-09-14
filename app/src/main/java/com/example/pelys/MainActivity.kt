package com.example.pelys

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pelys.ui.detail.MovieDetailScreen
import com.example.pelys.ui.favorites.FavoritesScreen
import com.example.pelys.ui.list.MovieListScreen
import com.example.pelys.ui.search.MovieSearchScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            Scaffold(
                bottomBar = {
                    val currentRoute = navController.currentBackStackEntryAsState().value
                        ?.destination?.route
                    NavigationBar {
                        NavigationBarItem(
                            selected = currentRoute == "list",
                            onClick = { navController.navigate("list") },
                            icon = { Icon(Icons.Default.Home, null) },
                            label = { Text("Populares") }
                        )
                        NavigationBarItem(
                            selected = currentRoute == "search",
                            onClick = { navController.navigate("search") },
                            icon = { Icon(Icons.Default.Search, null) },
                            label = { Text("Buscar") }
                        )
                        NavigationBarItem(
                            selected = currentRoute == "favorites",
                            onClick = { navController.navigate("favorites") },
                            icon = { Icon(Icons.Default.Favorite, null) },
                            label = { Text("Favoritos") }
                        )
                    }
                }
            ) { padding ->
                NavHost(
                    navController = navController,
                    startDestination = "list",
                    modifier = Modifier.padding(padding)
                ) {
                    composable("list") {
                        MovieListScreen(onMovieClick = { navController.navigate("detail/$it") })
                    }
                    composable("search") {
                        MovieSearchScreen(onMovieClick = { navController.navigate("detail/$it") })
                    }
                    composable("favorites") {
                        FavoritesScreen(onMovieClick = { navController.navigate("detail/$it") })
                    }
                    composable(
                        "detail/{movieId}",
                        arguments = listOf(navArgument("movieId") { type = NavType.IntType })
                    ) {
                        MovieDetailScreen(onBackClick = { navController.popBackStack() })
                    }
                }
            }
        }
    }
}