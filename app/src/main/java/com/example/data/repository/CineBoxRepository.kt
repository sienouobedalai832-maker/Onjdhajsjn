package com.example.data.repository

import com.example.data.local.CineBoxDao
import com.example.data.local.IPTVConfig
import com.example.data.local.User
import com.example.data.local.WatchHistory
import com.example.data.remote.NetworkModule
import com.example.data.remote.TmdbApi
import com.example.data.remote.XtreamApi
import com.example.data.remote.MediaItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

class CineBoxRepository(private val dao: CineBoxDao) {
    private val tmdbApi: TmdbApi = NetworkModule.tmdbApi
    private val xtreamApi: XtreamApi = NetworkModule.xtreamApi

    // Database operations
    suspend fun insertUser(user: User) = dao.insertUser(user)
    suspend fun updateUser(user: User) = dao.updateUser(user)
    suspend fun getUser(email: String) = dao.getUser(email)
    fun getAllUsers() = dao.getAllUsers()
    suspend fun setOnlineStatus(email: String, isOnline: Boolean) = dao.updateUserOnlineStatus(email, isOnline)
    
    suspend fun insertWatchHistory(history: WatchHistory) = dao.insertWatchHistory(history)
    fun getWatchHistoryForUser(email: String) = dao.getWatchHistoryForUser(email)
    
    suspend fun insertIptvConfig(config: IPTVConfig) = dao.insertIptvConfig(config)
    fun getIptvConfig() = dao.getIptvConfig()
    suspend fun getIptvConfigSync() = dao.getIptvConfigSync()

    suspend fun initializeIptvConfig() {
        val config = dao.getIptvConfigSync()
        if (config == null) {
            dao.insertIptvConfig(
                IPTVConfig(
                    serverUrl = "http://ipro2.pro2-ott.com:80",
                    username = "zuXG8Kb9",
                    password = "mjZ4XTZ",
                    backupUrl = "http://ipro2.pro2-ott.com:80"
                )
            )
        }
    }

    // TMDB API Calls
    suspend fun getTrendingMovies(): List<MediaItem> {
        val p1 = tmdbApi.getTrending(page = 1).results
        val p2 = tmdbApi.getTrending(page = 2).results
        return p1 + p2
    }
    
    suspend fun getPopularMovies(): List<MediaItem> {
        val p1 = tmdbApi.getPopularMovies(page = 1).results
        val p2 = tmdbApi.getPopularMovies(page = 2).results
        val p3 = tmdbApi.getPopularMovies(page = 3).results
        return p1 + p2 + p3
    }
    suspend fun getNewReleases() = tmdbApi.getNewReleases().results
    suspend fun getPopularSeries() = tmdbApi.getPopularSeries().results
    
    suspend fun getMoviesByGenre(genreId: Int): List<MediaItem> {
        val p1 = tmdbApi.getMoviesByGenre(genreId, page = 1).results
        val p2 = tmdbApi.getMoviesByGenre(genreId, page = 2).results
        return p1 + p2
    }
    suspend fun search(query: String) = tmdbApi.searchMulti(query).results
    suspend fun getMovieDetail(id: Int) = tmdbApi.getMovieDetail(id)
    suspend fun getSerieDetail(id: Int) = tmdbApi.getSerieDetail(id)
    suspend fun getSeasonDetail(id: Int, season: Int) = tmdbApi.getSeasonDetail(id, season)
    suspend fun getMovieCredits(id: Int) = tmdbApi.getMovieCredits(id).cast
    suspend fun getSimilarMovies(id: Int) = tmdbApi.getSimilarMovies(id).results

    // Xtream API Calls
    suspend fun authenticateXtream(config: IPTVConfig) = 
        xtreamApi.authenticate("${config.serverUrl}/player_api.php", config.username, config.password)
        
    suspend fun getXtreamCategories(config: IPTVConfig) = 
        xtreamApi.getCategories("${config.serverUrl}/player_api.php", config.username, config.password)
        
    suspend fun getXtreamStreams(config: IPTVConfig) = 
        xtreamApi.getStreams("${config.serverUrl}/player_api.php", config.username, config.password)
}
