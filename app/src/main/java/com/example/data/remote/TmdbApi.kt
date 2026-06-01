package com.example.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

@JsonClass(generateAdapter = true)
data class MediaResponse(
    val results: List<MediaItem>
)

@JsonClass(generateAdapter = true)
data class MediaItem(
    val id: Int,
    val title: String? = null,
    val name: String? = null,
    @Json(name = "poster_path") val posterPath: String? = null,
    @Json(name = "backdrop_path") val backdropPath: String? = null,
    val overview: String? = null,
    @Json(name = "vote_average") val voteAverage: Double? = null,
    @Json(name = "release_date") val releaseDate: String? = null,
    @Json(name = "first_air_date") val firstAirDate: String? = null,
    @Json(name = "media_type") val mediaType: String? = null
) {
    val displayTitle: String get() = title ?: name ?: "Unknown"
}

@JsonClass(generateAdapter = true)
data class MovieDetail(
    val id: Int,
    val title: String,
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "backdrop_path") val backdropPath: String?,
    val overview: String,
    @Json(name = "vote_average") val voteAverage: Double,
    @Json(name = "release_date") val releaseDate: String,
    val runtime: Int?,
    val genres: List<GenreResponse>
)

@JsonClass(generateAdapter = true)
data class SerieDetail(
    val id: Int,
    val name: String,
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "backdrop_path") val backdropPath: String?,
    val overview: String,
    @Json(name = "vote_average") val voteAverage: Double,
    @Json(name = "first_air_date") val firstAirDate: String,
    @Json(name = "number_of_seasons") val numberOfSeasons: Int,
    val genres: List<GenreResponse>
)

@JsonClass(generateAdapter = true)
data class SeasonDetail(
    @Json(name = "season_number") val seasonNumber: Int,
    val episodes: List<EpisodeResponse>
)

@JsonClass(generateAdapter = true)
data class EpisodeResponse(
    @Json(name = "episode_number") val episodeNumber: Int,
    val name: String
)

@JsonClass(generateAdapter = true)
data class GenreResponse(
    val id: Int,
    val name: String
)

@JsonClass(generateAdapter = true)
data class CreditsResponse(
    val cast: List<CastMember>
)

@JsonClass(generateAdapter = true)
data class CastMember(
    val id: Int,
    val name: String,
    val character: String,
    @Json(name = "profile_path") val profilePath: String?
)

interface TmdbApi {
    @GET("trending/all/day")
    suspend fun getTrending(@Query("language") language: String = "fr-FR", @Query("api_key") apiKey: String = "f71f7959dd2e0eae173514b0c99af2e6"): MediaResponse

    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("language") language: String = "fr-FR", @Query("api_key") apiKey: String = "f71f7959dd2e0eae173514b0c99af2e6"): MediaResponse

    @GET("movie/now_playing")
    suspend fun getNewReleases(@Query("language") language: String = "fr-FR", @Query("api_key") apiKey: String = "f71f7959dd2e0eae173514b0c99af2e6"): MediaResponse

    @GET("tv/popular")
    suspend fun getPopularSeries(@Query("language") language: String = "fr-FR", @Query("api_key") apiKey: String = "f71f7959dd2e0eae173514b0c99af2e6"): MediaResponse
    
    @GET("discover/movie")
    suspend fun getMoviesByGenre(@Query("with_genres") genreId: Int, @Query("language") language: String = "fr-FR", @Query("api_key") apiKey: String = "f71f7959dd2e0eae173514b0c99af2e6"): MediaResponse

    @GET("search/multi")
    suspend fun searchMulti(@Query("query") query: String, @Query("language") language: String = "fr-FR", @Query("api_key") apiKey: String = "f71f7959dd2e0eae173514b0c99af2e6"): MediaResponse
    
    @GET("movie/{id}")
    suspend fun getMovieDetail(@Path("id") id: Int, @Query("language") language: String = "fr-FR", @Query("api_key") apiKey: String = "f71f7959dd2e0eae173514b0c99af2e6"): MovieDetail

    @GET("movie/{id}/credits")
    suspend fun getMovieCredits(@Path("id") id: Int, @Query("language") language: String = "fr-FR", @Query("api_key") apiKey: String = "f71f7959dd2e0eae173514b0c99af2e6"): CreditsResponse

    @GET("movie/{id}/similar")
    suspend fun getSimilarMovies(@Path("id") id: Int, @Query("language") language: String = "fr-FR", @Query("api_key") apiKey: String = "f71f7959dd2e0eae173514b0c99af2e6"): MediaResponse
    
    @GET("tv/{id}")
    suspend fun getSerieDetail(@Path("id") id: Int, @Query("language") language: String = "fr-FR", @Query("api_key") apiKey: String = "f71f7959dd2e0eae173514b0c99af2e6"): SerieDetail

    @GET("tv/{id}/season/{season_number}")
    suspend fun getSeasonDetail(@Path("id") id: Int, @Path("season_number") seasonNumber: Int, @Query("language") language: String = "fr-FR", @Query("api_key") apiKey: String = "f71f7959dd2e0eae173514b0c99af2e6"): SeasonDetail
}
