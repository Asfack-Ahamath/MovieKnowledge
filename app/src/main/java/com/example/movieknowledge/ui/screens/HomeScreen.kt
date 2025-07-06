package com.example.movieknowledge.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.movieknowledge.R
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
    val isLoading by viewModel.isLoading.collectAsState()
    val message by viewModel.message.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(20.dp)
    ) {
        // Hero Section with Gradient Background
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                            )
                        )
                    )
                    .padding(24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.VideoLibrary,
                        contentDescription = "Movie Icon",
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Movie Knowledge",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Your personal movie database & search companion",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                }
            }
        }

        // Quick Actions Section
        Text(
            text = "Quick Actions",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Main Action Buttons Grid
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Primary Action - Add Movies
            ActionButton(
                onClick = onAddMoviesClick,
                icon = Icons.Default.Add,
                title = stringResource(R.string.add_movies),
                subtitle = "Populate your database with sample movies",
                isPrimary = true
            )

            // Search Actions Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ActionButton(
                    onClick = onSearchMoviesClick,
                    icon = Icons.Default.Search,
                    title = stringResource(R.string.search_movies),
                    subtitle = "Find movies online",
                    modifier = Modifier.weight(1f)
                )

                ActionButton(
                    onClick = onSearchActorsClick,
                    icon = Icons.Default.Person,
                    title = stringResource(R.string.search_actors),
                    subtitle = "Search by actor name",
                    modifier = Modifier.weight(1f)
                )
            }

            // Additional Actions Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ActionButton(
                    onClick = onSearchByTitleClick,
                    icon = Icons.Default.Movie,
                    title = stringResource(R.string.search_by_title),
                    subtitle = "Search by movie title",
                    modifier = Modifier.weight(1f)
                )

                ActionButton(
                    onClick = onFilterMoviesClick,
                    icon = Icons.Default.Sort,
                    title = stringResource(R.string.filter_movies),
                    subtitle = "Filter and browse",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Status Section
        if (isLoading || message != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Processing...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    message?.let { msg ->
                        val isError = msg.contains("Error", ignoreCase = true)
                        val isSuccess = msg.contains("successfully", ignoreCase = true) || 
                                       msg.contains("Database contains", ignoreCase = true)
                        
                        Icon(
                            imageVector = when {
                                isError -> Icons.Default.Warning
                                isSuccess -> Icons.Default.CheckCircle
                                else -> Icons.Default.Info
                            },
                            contentDescription = null,
                            tint = when {
                                isError -> MaterialTheme.colorScheme.error
                                isSuccess -> MaterialTheme.colorScheme.primary
                                else -> MaterialTheme.colorScheme.onSurfaceVariant
                            },
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = msg,
                            style = MaterialTheme.typography.bodyMedium,
                            color = when {
                                isError -> MaterialTheme.colorScheme.error
                                isSuccess -> MaterialTheme.colorScheme.primary
                                else -> MaterialTheme.colorScheme.onSurfaceVariant
                            },
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            // Welcome Tips Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Getting Started",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "1. Start by adding sample movies to your database\n" +
                              "2. Search for new movies online and save them\n" +
                              "3. Use actor search to find movies by cast\n" +
                              "4. Filter and browse your movie collection",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f),
                        lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.2
                    )
                }
            }
        }

        // Bottom Spacer
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionButton(
    onClick: () -> Unit,
    icon: ImageVector,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    isPrimary: Boolean = false
) {
    ElevatedCard(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (isPrimary) 
                MaterialTheme.colorScheme.primaryContainer 
            else 
                MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = if (isPrimary) 8.dp else 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (isPrimary)
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                else
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(12.dp),
                    tint = if (isPrimary)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = if (isPrimary) FontWeight.SemiBold else FontWeight.Medium,
                    color = if (isPrimary)
                        MaterialTheme.colorScheme.onPrimaryContainer
                    else
                        MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isPrimary)
                        MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    else
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Go",
                tint = if (isPrimary)
                    MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f)
                else
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

