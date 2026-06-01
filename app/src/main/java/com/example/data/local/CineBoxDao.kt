package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CineBoxDao {
    // USERS
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUser(email: String): User?

    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<User>>

    @Query("UPDATE users SET isOnline = :isOnline WHERE email = :email")
    suspend fun updateUserOnlineStatus(email: String, isOnline: Boolean)

    // WATCH HISTORY
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchHistory(history: WatchHistory)

    @Query("SELECT * FROM watch_history WHERE userEmail = :email ORDER BY watchedDate DESC")
    fun getWatchHistoryForUser(email: String): Flow<List<WatchHistory>>

    // IPTV CONFIG
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIptvConfig(config: IPTVConfig)

    @Query("SELECT * FROM iptv_config WHERE id = 1")
    fun getIptvConfig(): Flow<IPTVConfig?>
    
    @Query("SELECT * FROM iptv_config WHERE id = 1")
    suspend fun getIptvConfigSync(): IPTVConfig?
}
