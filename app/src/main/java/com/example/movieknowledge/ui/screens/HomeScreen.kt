package com.example.movieknowledge.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.movieknowledge.R
import com.example.movieknowledge.data.local.entities.Movie
import com.example.movieknowledge.ui.components.MovieItem
import com.example.movieknowledge.viewmodel.MovieViewModel

@Composable
fun HomeScreen(
    viewModel: MovieViewModel,
    onAddMoviesClick: () -> Unit,
    onSearchMoviesClick: () -> Unit,
    onSearchActorsClick: () -> Unit,
    onSearchByTitleClick: () -> Unit,
    onFilterMoviesClick: () -> Unit
) {
    val allSavedMovies by viewModel.allSavedMovies.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val message by viewModel.message.collectAsState()
    val listState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Movie Knowledge App",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        // Navigation buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onAddMoviesClick,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = stringResource(R.string.add_movies))
            }

            Button(
                onClick = onSearchMoviesClick,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = stringResource(R.string.search_movies))
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onSearchActorsClick,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = stringResource(R.string.search_actors))
            }

            Button(
                onClick = onSearchByTitleClick,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = stringResource(R.string.search_by_title))
            }
        }

        Button(
            onClick = onFilterMoviesClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = stringResource(R.string.filter_movies))
        }

        // Loading indicator
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        // Message display
        message?.let { msg ->
            val isError = msg.contains("Error", ignoreCase = true)
            val isSuccess = msg.contains("successfully", ignoreCase = true) || 
                           msg.contains("Database contains", ignoreCase = true)
            
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = when {
                        isError -> MaterialTheme.colorScheme.errorContainer
                        isSuccess -> MaterialTheme.colorScheme.primaryContainer
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    }
                )
            ) {
                Text(
                    text = msg,
                    style = MaterialTheme.typography.bodyMedium,
                    color = when {
                        isError -> MaterialTheme.colorScheme.onErrorContainer
                        isSuccess -> MaterialTheme.colorScheme.onPrimaryContainer
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }

        // Saved movies display
        if (allSavedMovies.isNotEmpty()) {
            Text(
                text = "Saved Movies Database (${allSavedMovies.size} movies)",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            LazyColumn(
                state = listState,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(allSavedMovies) { movie ->
                    MovieItem(
                        movie = movie,
                        onSaveClick = null // No save button needed for already saved movies
                    )
                }
            }
        } else if (!isLoading && message == null) {
            // Empty state when no movies are displayed
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No movies in database yet",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Click 'Add Movies to DB' to populate with sample movies",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}