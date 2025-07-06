package com.example.movieknowledge.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.movieknowledge.R

@Composable
fun HomeScreen(
    onAddMoviesClick: () -> Unit,
    onSearchMoviesClick: () -> Unit,
    onSearchActorsClick: () -> Unit,
    onSearchByTitleClick: () -> Unit,
    onFilterMoviesClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Movie Knowledge App",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Button(
            onClick = onAddMoviesClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = stringResource(R.string.add_movies))
        }

        Button(
            onClick = onSearchMoviesClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = stringResource(R.string.search_movies))
        }

        Button(
            onClick = onSearchActorsClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = stringResource(R.string.search_actors))
        }

        Button(
            onClick = onSearchByTitleClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = stringResource(R.string.search_by_title))
        }

        Button(
            onClick = onFilterMoviesClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = stringResource(R.string.filter_movies))
        }
    }
}