package com.example.movieknowledge.data.remote

import com.example.movieknowledge.data.remote.models.MovieResponse
import com.example.movieknowledge.data.remote.models.SearchResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApi {
    companion object {
        private const val API_KEY = "55a5c3f6"
        private const val BASE_URL = "https://www.omdbapi.com"

        fun create(): OmdbApi {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OmdbApi::class.java)
        }
    }

    @GET("/")
    suspend fun getMovieByTitle(
        @Query("t") title: String,
        @Query("apikey") apiKey: String = API_KEY
    ): MovieResponse

    @GET("/")
    suspend fun getMovieById(
        @Query("i") imdbId: String,
        @Query("apikey") apiKey: String = API_KEY
    ): MovieResponse

    @GET("/")
    suspend fun searchMovies(
        @Query("s") searchTerm: String,
        @Query("apikey") apiKey: String = API_KEY
    ): SearchResponse
}