package com.example.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.IPTVConfig
import com.example.data.local.User
import com.example.data.local.WatchHistory
import com.example.data.remote.MediaItem
import com.example.data.remote.XtreamCategory
import com.example.data.remote.XtreamStream
import com.example.data.repository.CineBoxRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class AppViewModel(private val repository: CineBoxRepository) : ViewModel() {
    
    // Auth State
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()
    
    fun checkAutoLogin(email: String) {
        viewModelScope.launch {
            val user = repository.getUser(email)
            if (user != null) {
                repository.setOnlineStatus(email, true)
                _currentUser.value = user
            }
        }
    }
    
    fun login(email: String, name: String) {
        viewModelScope.launch {
            var user = repository.getUser(email)
            if (user == null) {
                user = User(email, "hashed", name, System.currentTimeMillis(), true)
                repository.insertUser(user)
            } else {
                repository.setOnlineStatus(email, true)
            }
            _currentUser.value = user
        }
    }
    
    fun logout() {
        viewModelScope.launch {
            _currentUser.value?.let {
                repository.setOnlineStatus(it.email, false)
            }
            _currentUser.value = null
        }
    }

    // Media State
    private val _trending = MutableStateFlow<List<MediaItem>>(emptyList())
    val trending = _trending.asStateFlow()

    private val _popularMovies = MutableStateFlow<List<MediaItem>>(emptyList())
    val popularMovies = _popularMovies.asStateFlow()
    
    private val _genreMovies = MutableStateFlow<List<MediaItem>>(emptyList())
    val genreMovies = _genreMovies.asStateFlow()
    
    private val _searchResults = MutableStateFlow<List<MediaItem>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    fun searchMovies(query: String) {
        if (query.isEmpty()) {
            _searchResults.value = emptyList()
            return
        }
        viewModelScope.launch {
            try {
                _searchResults.value = repository.search(query)
            } catch (e: Exception) {
                e.printStackTrace()
                _searchResults.value = emptyList()
            }
        }
    }

    fun loadMoviesByGenre(genreId: Int) {
        viewModelScope.launch {
            try {
                _genreMovies.value = repository.getMoviesByGenre(genreId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    init {
        viewModelScope.launch {
            repository.initializeIptvConfig()
            try {
                _trending.value = repository.getTrendingMovies()
                _popularMovies.value = repository.getPopularMovies()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Xtream State
    private val _liveCategories = MutableStateFlow<List<XtreamCategory>>(emptyList())
    val liveCategories = _liveCategories.asStateFlow()
    
    private val _liveStreams = MutableStateFlow<List<XtreamStream>>(emptyList())
    val liveStreams = _liveStreams.asStateFlow()
    
    private val _xtreamAuth = MutableStateFlow<Boolean?>(null)
    val xtreamAuth = _xtreamAuth.asStateFlow()

    fun loadXtreamConfigAndAuth() {
        viewModelScope.launch {
            val config = repository.getIptvConfig().firstOrNull()
            if (config != null) {
                try {
                    val authRes = repository.authenticateXtream(config)
                    val authSuccess = authRes.userInfo?.auth == 1
                    _xtreamAuth.value = authSuccess
                    if (authSuccess) {
                        _liveCategories.value = repository.getXtreamCategories(config)
                        _liveStreams.value = repository.getXtreamStreams(config)
                    }
                } catch (e: Exception) {
                    _xtreamAuth.value = false
                }
            }
        }
    }

    fun saveHistory(mediaId: Int, title: String, poster: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.insertWatchHistory(
                WatchHistory(
                    userEmail = user.email,
                    mediaId = mediaId,
                    title = title,
                    posterPath = poster,
                    watchedDate = System.currentTimeMillis()
                )
            )
        }
    }
    
    fun getAllUsers() = repository.getAllUsers()
    
    fun getIptvConfig() = repository.getIptvConfig()
    
    suspend fun insertIptvConfig(config: IPTVConfig) = repository.insertIptvConfig(config)
    
    // We will just expose the repository functions for direct one-time calls in Compose where necessary, 
    // or build smaller ViewModels for specific screens if needed to avoid bloating this one.
    suspend fun getMovieDetail(id: Int) = repository.getMovieDetail(id)
    suspend fun getSerieDetail(id: Int) = repository.getSerieDetail(id)
    suspend fun getSeriesSeasons(id: Int, season: Int) = repository.getSeasonDetail(id, season)
    suspend fun getSimilar(id: Int) = repository.getSimilarMovies(id)
    suspend fun getCredits(id: Int) = repository.getMovieCredits(id)
}
