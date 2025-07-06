package com.example.movieknowledge.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieknowledge.data.local.entities.Movie
import com.example.movieknowledge.data.remote.models.MovieResponse
import com.example.movieknowledge.data.remote.models.SearchResult
import com.example.movieknowledge.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel that handles movie related data and operations
 */
class MovieViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "MovieViewModel"
    private val repository = MovieRepository(application)

    // StateFlow for UI states
    private val _currentMovie = MutableStateFlow<MovieResponse?>(null)
    val currentMovie = _currentMovie.asStateFlow()

    private val _actorSearchResults = MutableStateFlow<List<Movie>>(emptyList())
    val actorSearchResults = _actorSearchResults.asStateFlow()

    private val _titleSearchResults = MutableStateFlow<List<SearchResult>>(emptyList())
    val titleSearchResults = _titleSearchResults.asStateFlow()

    private val _savedMovies = MutableStateFlow<List<Movie>>(emptyList())
    val savedMovies = _savedMovies.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message = _message.asStateFlow()

    /**
     * Check if database needs to be initialized and do so if needed
     */
    fun checkAndInitDatabase() {
        viewModelScope.launch {
            try {
                val count = repository.getMoviesCount()
                Log.d(TAG, "Current database movie count: $count")
                if (count == 0) {
                    _message.value = "Initializing database with sample movies..."
                    repository.addMoviesToDb()
                    _message.value = "Database initialized with sample movies"
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error checking database: ${e.message}")
                _message.value = "Error checking database: ${e.message}"
            }
        }
    }

    /**
     * Add predefined movies to database
     */
    fun addMoviesToDb(onComplete: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val count = repository.getMoviesCount()
                if (count > 0) {
                    _message.value = "Movies already in database (${count} movies)"
                    // Load existing movies to show them
                    loadSavedMovies()
                    onComplete(true)
                } else {
                    repository.addMoviesToDb()
                    _message.value = "Movies added to database successfully"
                    // Load the newly added movies
                    loadSavedMovies()
                    onComplete(true)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error adding movies: ${e.message}")
                _message.value = "Error adding movies: ${e.message}"
                onComplete(false)
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Get movie by title from API
     */
    fun getMovieByTitle(title: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = repository.getMovieByTitle(title)
                result.fold(
                    onSuccess = {
                        _currentMovie.value = it
                        Log.d(TAG, "Successfully retrieved movie: ${it.title}")
                    },
                    onFailure = {
                        _message.value = "Error: ${it.message}"
                        Log.e(TAG, "Error retrieving movie: ${it.message}")
                    }
                )
            } catch (e: Exception) {
                _message.value = "Error retrieving movie: ${e.message}"
                Log.e(TAG, "Exception in getMovieByTitle: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Save current movie to database
     */
    fun saveCurrentMovieToDb() {
        viewModelScope.launch {
            _isLoading.value = true
            _message.value = null // Clear previous messages
            try {
                currentMovie.value?.let { movie ->
                    Log.d(TAG, "Attempting to save movie: ${movie.title} (IMDb: ${movie.imdbID})")
                    repository.saveMovieToDb(movie)
                    _message.value = "Movie '${movie.title}' saved to database successfully!"
                    Log.d(TAG, "Successfully saved movie to database: ${movie.title}")
                } ?: run {
                    _message.value = "No movie to save. Please search for a movie first."
                    Log.w(TAG, "Attempted to save movie but no movie is currently loaded")
                }
            } catch (e: IllegalArgumentException) {
                _message.value = "Invalid movie data: ${e.message}"
                Log.e(TAG, "Invalid movie data: ${e.message}")
            } catch (e: Exception) {
                _message.value = "Error saving movie: ${e.message ?: "Unknown error occurred"}"
                Log.e(TAG, "Error saving movie: ${e.message}", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Search movies by actor
     * Gets a single result from repository and updates the UI state
     */
    fun searchMoviesByActor(actorName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _message.value = null // Clear previous messages
            try {
                Log.d(TAG, "Searching for actor: $actorName")

                // Check if database is initialized first
                val count = repository.getMoviesCount()
                if (count == 0) {
                    _message.value = "Initializing database with sample movies..."
                    repository.addMoviesToDb()
                    Log.d(TAG, "Database initialized with movies")
                }

                // Now perform the search using first() to get a single emission
                val movies = repository.searchMoviesByActor(actorName).first()
                _actorSearchResults.value = movies
                Log.d(TAG, "Found ${movies.size} movies with actor: $actorName")

                if (movies.isEmpty()) {
                    _message.value = "No movies found with actor: $actorName"
                } else {
                    _message.value = null // Clear error message if movies found
                }
            } catch (e: Exception) {
                _message.value = "Error searching movies: ${e.message}"
                Log.e(TAG, "Exception in searchMoviesByActor: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Search movies by title from API
     */
    fun searchMoviesByTitle(title: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = repository.searchMoviesByTitle(title)
                result.fold(
                    onSuccess = { results ->
                        _titleSearchResults.value = results
                        if (results.isEmpty()) {
                            _message.value = "No movies found with title: $title"
                        }
                        Log.d(TAG, "Found ${results.size} movies matching title: $title")
                    },
                    onFailure = {
                        _message.value = "Error: ${it.message}"
                        Log.e(TAG, "Error searching movies by title: ${it.message}")
                    }
                )
            } catch (e: Exception) {
                _message.value = "Error searching movies: ${e.message}"
                Log.e(TAG, "Exception in searchMoviesByTitle: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Get details for a specific movie by IMDb ID
     */
    fun getMovieDetails(imdbId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = repository.getMovieDetails(imdbId)
                result.fold(
                    onSuccess = { _currentMovie.value = it },
                    onFailure = { _message.value = "Error: ${it.message}" }
                )
            } catch (e: Exception) {
                _message.value = "Error retrieving movie details: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Clear current movie
     */
    fun clearCurrentMovie() {
        _currentMovie.value = null
    }

    /**
     * Clear message
     */
    fun clearMessage() {
        _message.value = null
    }

    /**
     * Clear search results for actors
     */
    fun clearActorSearchResults() {
        _actorSearchResults.value = emptyList()
    }

    /**
     * Clear search results for titles
     */
    fun clearTitleSearchResults() {
        _titleSearchResults.value = emptyList()
    }

    /**
     * Get all movies from database for verification
     */
    fun getAllMoviesFromDb() {
        viewModelScope.launch {
            try {
                val count = repository.getMoviesCount()
                _message.value = "Database contains $count movies"
                Log.d(TAG, "Database verification: $count movies found")
            } catch (e: Exception) {
                _message.value = "Error checking database: ${e.message}"
                Log.e(TAG, "Error checking database: ${e.message}")
            }
        }
    }

    /**
     * Load all saved movies from database
     */
    fun loadSavedMovies() {
        viewModelScope.launch {
            _isLoading.value = true
            _message.value = null
            try {
                Log.d(TAG, "Loading saved movies from database")
                val movies = repository.getAllMovies().first()
                _savedMovies.value = movies
                Log.d(TAG, "Loaded ${movies.size} movies from database")
                
                if (movies.isEmpty()) {
                    _message.value = "No movies found in database"
                } else {
                    _message.value = "Loaded ${movies.size} movies from database"
                }
            } catch (e: Exception) {
                _message.value = "Error loading movies: ${e.message}"
                Log.e(TAG, "Error loading saved movies: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Clear saved movies list
     */
    fun clearSavedMovies() {
        _savedMovies.value = emptyList()
    }
}