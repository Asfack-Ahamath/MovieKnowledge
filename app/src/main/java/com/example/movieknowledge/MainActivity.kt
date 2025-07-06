package com.example.movieknowledge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.movieknowledge.ui.screens.HomeScreen
import com.example.movieknowledge.ui.screens.SearchActorsScreen
import com.example.movieknowledge.ui.screens.SearchByTitleScreen
import com.example.movieknowledge.ui.screens.SearchMoviesScreen
import com.example.movieknowledge.ui.screens.FilterMoviesScreen
import com.example.movieknowledge.ui.theme.MovieKnowledgeAppTheme
import com.example.movieknowledge.viewmodel.MovieViewModel

/**
 * I confirm that I understand what plagiarism is and have read and understood the
 * section on Assessment Offences in the Essential Information for Students. The work
 * that I have submitted is entirely my own. Any work from other authors is duly
 * referenced and acknowledged.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieKnowledgeAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val viewModel = MovieViewModel(application)
                    MovieNavigation(navController, viewModel)
                }
            }
        }
    }
}

@Composable
fun MovieNavigation(navController: NavHostController, viewModel: MovieViewModel) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                onAddMoviesClick = { viewModel.addMoviesToDb() },
                onSearchMoviesClick = { navController.navigate("search_movies") },
                onSearchActorsClick = { navController.navigate("search_actors") },
                onSearchByTitleClick = { navController.navigate("search_by_title") },
                onFilterMoviesClick = { navController.navigate("filter_movies") }
            )
        }
        composable("search_movies") {
            SearchMoviesScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("search_actors") {
            SearchActorsScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("search_by_title") {
            SearchByTitleScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("filter_movies") {
            FilterMoviesScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}