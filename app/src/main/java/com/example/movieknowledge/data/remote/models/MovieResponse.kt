package com.example.movieknowledge.data.remote.models

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("Title") val title: String = "",
    @SerializedName("Year") val year: String = "",
    @SerializedName("Rated") val rated: String = "",
    @SerializedName("Released") val released: String = "",
    @SerializedName("Runtime") val runtime: String = "",
    @SerializedName("Genre") val genre: String = "",
    @SerializedName("Director") val director: String = "",
    @SerializedName("Writer") val writer: String = "",
    @SerializedName("Actors") val actors: String = "",
    @SerializedName("Plot") val plot: String = "",
    @SerializedName("Language") val language: String = "",
    @SerializedName("Country") val country: String = "",
    @SerializedName("Awards") val awards: String = "",
    @SerializedName("Poster") val poster: String = "",
    @SerializedName("Ratings") val ratings: List<Rating> = emptyList(),
    @SerializedName("Metascore") val metascore: String = "",
    @SerializedName("imdbRating") val imdbRating: String = "",
    @SerializedName("imdbVotes") val imdbVotes: String = "",
    @SerializedName("imdbID") val imdbID: String = "",
    @SerializedName("Type") val type: String = "",
    @SerializedName("DVD") val dvd: String = "",
    @SerializedName("BoxOffice") val boxOffice: String = "",
    @SerializedName("Production") val production: String = "",
    @SerializedName("Website") val website: String = "",
    @SerializedName("Response") val response: String = "",
    @SerializedName("Error") val error: String? = null,
    @SerializedName("totalSeasons") val totalSeasons: String? = null
)

data class Rating(
    @SerializedName("Source") val source: String,
    @SerializedName("Value") val value: String
)

data class SearchResponse(
    @SerializedName("Search") val search: List<SearchResult> = emptyList(),
    @SerializedName("totalResults") val totalResults: String = "0",
    @SerializedName("Response") val response: String = "",
    @SerializedName("Error") val error: String? = null
)

data class SearchResult(
    @SerializedName("Title") val title: String,
    @SerializedName("Year") val year: String,
    @SerializedName("imdbID") val imdbID: String,
    @SerializedName("Type") val type: String,
    @SerializedName("Poster") val poster: String
)