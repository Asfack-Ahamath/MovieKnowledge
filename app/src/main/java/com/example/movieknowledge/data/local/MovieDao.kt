package com.example.movieknowledge.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movieknowledge.data.local.entities.Movie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies")
    fun getAllMovies(): Flow<List<Movie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<Movie>)

    @Query("SELECT * FROM movies WHERE actors LIKE '%' || :actorName || '%' COLLATE NOCASE")
    fun searchMoviesByActor(actorName: String): Flow<List<Movie>>

    @Query("SELECT COUNT(*) FROM movies")
    suspend fun getMoviesCount(): Int
}