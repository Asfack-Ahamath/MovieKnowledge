package com.example.movieknowledge.data.repository

import android.content.Context
import android.util.Log
import com.example.movieknowledge.data.local.AppDatabase
import com.example.movieknowledge.data.local.entities.Movie
import com.example.movieknowledge.data.remote.OmdbApi
import com.example.movieknowledge.data.remote.models.MovieResponse
import com.example.movieknowledge.data.remote.models.SearchResult
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

/**
 * Repository class that acts as a single source of truth for data.
 * Handles communication with both the local database and the remote API.
 */
class MovieRepository(private val context: Context) {
    private val TAG = "MovieRepository"
    private val movieDao = AppDatabase.getDatabase(context).movieDao()
    private val api = OmdbApi.create()

    /**
     * Get all movies from database
     */
    fun getAllMovies(): Flow<List<Movie>> {
        return movieDao.getAllMovies()
    }

    /**
     * Search movies by actor name (case insensitive, partial match)
     */
    fun searchMoviesByActor(actorName: String): Flow<List<Movie>> {
        return movieDao.searchMoviesByActor(actorName)
    }

    /**
     * Get movie count in database
     */
    suspend fun getMoviesCount(): Int {
        return movieDao.getMoviesCount()
    }

    /**
     * Add predefined movies to database from assets
     */
    suspend fun addMoviesToDb() {
        withContext(Dispatchers.IO) {
            try {
                // Read movies from assets file
                Log.d(TAG, "Starting to read movies from assets")
                val moviesJson = context.assets.open("movies.txt").bufferedReader().use { it.readText() }
                val movieResponses = moviesJson.split("\n\n").filter { it.isNotBlank() }

                Log.d(TAG, "Found ${movieResponses.size} movies in assets")

                // Parse each movie and convert to entity
                val movies = movieResponses.mapNotNull { json ->
                    try {
                        val response = Gson().fromJson(json, MovieResponse::class.java)
                        Movie(
                            imdbID = response.imdbID,
                            title = response.title,
                            year = response.year,
                            rated = response.rated,
                            released = response.released,
                            runtime = response.runtime,
                            genre = response.genre,
                            director = response.director,
                            writer = response.writer,
                            actors = response.actors,
                            plot = response.plot,
                            language = response.language,
                            country = response.country,
                            awards = response.awards,
                            poster = response.poster,
                            imdbRating = response.imdbRating,
                            imdbVotes = response.imdbVotes,
                            type = response.type
                        )
                    } catch (e: Exception) {
                        Log.e(TAG, "Error parsing movie: ${e.message}")
                        null
                    }
                }

                Log.d(TAG, "Successfully parsed ${movies.size} movies, inserting into database")

                // Insert all movies into database
                movieDao.insertMovies(movies)
                Log.d(TAG, "Successfully added movies to database")
            } catch (e: Exception) {
                Log.e(TAG, "Error adding movies to DB: ${e.message}")
                throw e
            }
        }
    }

    /**
     * Get movie by title from API
     */
    suspend fun getMovieByTitle(title: String): Result<MovieResponse> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Fetching movie with title: $title")
                val response = api.getMovieByTitle(title)

                if (response.response == "True") {
                    Log.d(TAG, "Successfully retrieved movie: ${response.title}")
                    Result.success(response)
                } else {
                    val errorMsg = response.error ?: "Movie not found"
                    Log.e(TAG, "API error: $errorMsg")

                    // Check if error is authentication related
                    if (errorMsg.contains("Invalid API key", ignoreCase = true) ||
                        errorMsg.contains("Unauthorized", ignoreCase = true)) {
                        Result.failure(Exception("API authentication failed. Please check your API key."))
                    } else {
                        Result.failure(Exception(errorMsg))
                    }
                }
            } catch (e: HttpException) {
                Log.e(TAG, "HTTP error: ${e.code()} - ${e.message()}")

                // Handle HTTP errors specifically
                when (e.code()) {
                    401 -> Result.failure(Exception("API authentication failed. Please check your API key."))
                    429 -> Result.failure(Exception("Too many requests to the API. Please try again later."))
                    else -> Result.failure(Exception("Network error: ${e.message}"))
                }
            } catch (e: IOException) {
                Log.e(TAG, "Network error: ${e.message}")
                Result.failure(Exception("Network error: Please check your internet connection."))
            } catch (e: Exception) {
                Log.e(TAG, "Error retrieving movie: ${e.message}")
                Result.failure(e)
            }
        }
    }

    /**
     * Save movie to database
     */
    suspend fun saveMovieToDb(movie: MovieResponse) {
        withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Saving movie to database: ${movie.title} with IMDb ID: ${movie.imdbID}")

                // Validate required fields
                if (movie.imdbID.isBlank()) {
                    throw IllegalArgumentException("Movie IMDb ID cannot be blank")
                }
                if (movie.title.isBlank()) {
                    throw IllegalArgumentException("Movie title cannot be blank")
                }

                // Convert API response to database entity
                val movieEntity = Movie(
                    imdbID = movie.imdbID,
                    title = movie.title,
                    year = movie.year.ifBlank { "Unknown" },
                    rated = movie.rated.ifBlank { "Not Rated" },
                    released = movie.released.ifBlank { "Unknown" },
                    runtime = movie.runtime.ifBlank { "Unknown" },
                    genre = movie.genre.ifBlank { "Unknown" },
                    director = movie.director.ifBlank { "Unknown" },
                    writer = movie.writer.ifBlank { "Unknown" },
                    actors = movie.actors.ifBlank { "Unknown" },
                    plot = movie.plot.ifBlank { "No plot available" },
                    language = movie.language.ifBlank { "Unknown" },
                    country = movie.country.ifBlank { "Unknown" },
                    awards = movie.awards.ifBlank { "N/A" },
                    poster = movie.poster.ifBlank { "" },
                    imdbRating = movie.imdbRating.ifBlank { "N/A" },
                    imdbVotes = movie.imdbVotes.ifBlank { "N/A" },
                    type = movie.type.ifBlank { "movie" }
                )

                Log.d(TAG, "Movie entity created successfully, inserting into database...")

                // Insert into database
                movieDao.insertMovie(movieEntity)
                
                // Verify the movie was saved
                val count = movieDao.getMoviesCount()
                Log.d(TAG, "Successfully saved movie to database. Total movies in DB: $count")
            } catch (e: IllegalArgumentException) {
                Log.e(TAG, "Invalid movie data: ${e.message}")
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Error saving movie to DB: ${e.message}", e)
                throw e
            }
        }
    }

    /**
     * Search movies by title from API
     */
    suspend fun searchMoviesByTitle(title: String): Result<List<SearchResult>> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Searching movies with title: $title")
                val response = api.searchMovies(title)

                if (response.response == "True") {
                    Log.d(TAG, "Found ${response.search.size} movies matching the title")
                    Result.success(response.search)
                } else {
                    val errorMsg = response.error ?: "No movies found"
                    Log.e(TAG, "API error: $errorMsg")

                    // Check if this is a "not found" error or something more serious
                    if (errorMsg.contains("not found", ignoreCase = true) ||
                        errorMsg.contains("too many", ignoreCase = true)) {
                        Result.failure(Exception(errorMsg))
                    } else if (errorMsg.contains("Invalid API key", ignoreCase = true) ||
                        errorMsg.contains("Unauthorized", ignoreCase = true)) {
                        Result.failure(Exception("API authentication failed. Please check your API key."))
                    } else {
                        Result.failure(Exception(errorMsg))
                    }
                }
            } catch (e: HttpException) {
                Log.e(TAG, "HTTP error: ${e.code()} - ${e.message()}")

                // Handle HTTP errors specifically
                when (e.code()) {
                    401 -> Result.failure(Exception("API authentication failed. Please check your API key."))
                    429 -> Result.failure(Exception("Too many requests to the API. Please try again later."))
                    else -> Result.failure(Exception("Network error: ${e.message}"))
                }
            } catch (e: IOException) {
                Log.e(TAG, "Network error: ${e.message}")
                Result.failure(Exception("Network error: Please check your internet connection."))
            } catch (e: Exception) {
                Log.e(TAG, "Error searching movies: ${e.message}")
                Result.failure(e)
            }
        }
    }

    /**
     * Get detailed movie information by IMDb ID from API
     */
    suspend fun getMovieDetails(imdbId: String): Result<MovieResponse> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Fetching movie details with IMDb ID: $imdbId")
                val response = api.getMovieById(imdbId)

                if (response.response == "True") {
                    Log.d(TAG, "Successfully retrieved movie details: ${response.title}")
                    Result.success(response)
                } else {
                    val errorMsg = response.error ?: "Movie details not found"
                    Log.e(TAG, "API error: $errorMsg")
                    Result.failure(Exception(errorMsg))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error retrieving movie details: ${e.message}")
                Result.failure(e)
            }
        }
    }
}