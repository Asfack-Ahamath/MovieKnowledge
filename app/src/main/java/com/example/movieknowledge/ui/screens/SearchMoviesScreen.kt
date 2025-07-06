package com.example.movieknowledge.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.movieknowledge.R
import com.example.movieknowledge.viewmodel.MovieViewModel

@Composable
fun SearchMoviesScreen(
    viewModel: MovieViewModel,
    onBackClick: () -> Unit
) {
    val currentMovie by viewModel.currentMovie.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val message by viewModel.message.collectAsState()

    var searchText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.clearCurrentMovie()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = stringResource(R.string.search_movies),
                style = MaterialTheme.typography.headlineMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Movie Title") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    if (searchText.isNotBlank()) {
                        viewModel.getMovieByTitle(searchText)
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = stringResource(R.string.retrieve_movie))
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { viewModel.saveCurrentMovieToDb() },
                modifier = Modifier.weight(1f),
                enabled = currentMovie != null
            ) {
                Text(text = stringResource(R.string.save_movie))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        message?.let { msg ->
            val isError = msg.contains("Error", ignoreCase = true) || 
                         msg.contains("Invalid", ignoreCase = true) ||
                         msg.contains("No movie to save", ignoreCase = true)
            val isSuccess = msg.contains("saved", ignoreCase = true) || 
                           msg.contains("successfully", ignoreCase = true)
            
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = when {
                        isSuccess -> MaterialTheme.colorScheme.primaryContainer
                        isError -> MaterialTheme.colorScheme.errorContainer
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    }
                )
            ) {
                Text(
                    text = msg,
                    style = MaterialTheme.typography.bodyMedium,
                    color = when {
                        isSuccess -> MaterialTheme.colorScheme.onPrimaryContainer
                        isError -> MaterialTheme.colorScheme.onErrorContainer
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    modifier = Modifier.padding(16.dp)
                )
            }
            
            LaunchedEffect(msg) {
                // Clear message after 5 seconds for success, 3 seconds for others
                kotlinx.coroutines.delay(if (isSuccess) 5000L else 3000L)
                viewModel.clearMessage()
            }
        }

        currentMovie?.let { movie ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(top = 16.dp)
            ) {
                MovieDetailItem("Title:", movie.title)
                MovieDetailItem("Year:", movie.year)
                MovieDetailItem("Rated:", movie.rated)
                MovieDetailItem("Released:", movie.released)
                MovieDetailItem("Runtime:", movie.runtime)
                MovieDetailItem("Genre:", movie.genre)
                MovieDetailItem("Director:", movie.director)
                MovieDetailItem("Writer:", movie.writer)
                MovieDetailItem("Actors:", movie.actors)
                MovieDetailItem("Plot:", movie.plot)
            }
        }
    }
}

@Composable
fun MovieDetailItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.width(80.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}