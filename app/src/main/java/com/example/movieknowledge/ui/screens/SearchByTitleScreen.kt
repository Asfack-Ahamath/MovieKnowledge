package com.example.movieknowledge.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.movieknowledge.R
import com.example.movieknowledge.data.remote.models.SearchResult
import com.example.movieknowledge.viewmodel.MovieViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchByTitleScreen(
    viewModel: MovieViewModel,
    onBackClick: () -> Unit
) {
    val searchResults by viewModel.titleSearchResults.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val message by viewModel.message.collectAsState()

    var searchText by remember { mutableStateOf("") }
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Track if the scroll button should be shown
    val showScrollToTopButton by remember {
        derivedStateOf {
            scrollState.firstVisibleItemIndex > 0
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.search_by_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            // Show scroll to top button when needed
            if (showScrollToTopButton) {
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            scrollState.animateScrollToItem(0)
                        }
                    }
                ) {
                    Icon(
                        Icons.Default.KeyboardArrowUp,
                        contentDescription = "Scroll to top"
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // Search field
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Movie Title") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                trailingIcon = {
                    IconButton(
                        onClick = {
                            if (searchText.isNotBlank()) {
                                viewModel.searchMoviesByTitle(searchText)
                            }
                        }
                    ) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Search button
            Button(
                onClick = {
                    if (searchText.isNotBlank()) {
                        viewModel.searchMoviesByTitle(searchText)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = searchText.isNotBlank()
            ) {
                Text(text = stringResource(R.string.search))
            }

            Spacer(modifier = Modifier.height(8.dp))

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

            // Error or info message
            message?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
                LaunchedEffect(it) {
                    // Clear message after 3 seconds
                    kotlinx.coroutines.delay(3000L)
                    viewModel.clearMessage()
                }
            }

            // Results section
            if (searchResults.isNotEmpty()) {
                Text(
                    text = "Found ${searchResults.size} results",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                // LazyColumn for results with scroll state
                LazyColumn(
                    state = scrollState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(bottom = 80.dp) // Add bottom padding for FAB
                ) {
                    items(searchResults) { movie ->
                        MovieResultCard(movie)
                    }
                }
            } else if (!isLoading && message == null) {
                // Empty state
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Search for movies by title",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieResultCard(movie: SearchResult) {
    ElevatedCard(
        onClick = { /* Could navigate to movie details */ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = movie.title,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Year: ${movie.year}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = movie.type.capitalize(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "IMDb ID: ${movie.imdbID}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

// Extension function to capitalize first letter
private fun String.capitalize(): String {
    return this.replaceFirstChar { it.uppercase() }
}