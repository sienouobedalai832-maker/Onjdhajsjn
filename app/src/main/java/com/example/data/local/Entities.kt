package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val email: String,
    val passwordHash: String,
    val name: String,
    val registrationDate: Long,
    val isOnline: Boolean,
    val profilePhotoUri: String? = null
)

@Entity(tableName = "watch_history")
data class WatchHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userEmail: String,
    val mediaId: Int, // TMDB ID
    val title: String,
    val posterPath: String,
    val watchedDate: Long
)

@Entity(tableName = "iptv_config")
data class IPTVConfig(
    @PrimaryKey val id: Int = 1,
    val serverUrl: String,
    val username: String,
    val password: String,
    val region: String = "fr",
    val expirationDate: Long = 0,
    val backupUrl: String = ""
)
