package com.example.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

@JsonClass(generateAdapter = true)
data class XtreamUserInfoResponse(
    @Json(name = "user_info") val userInfo: XtreamUserInfo?
)

@JsonClass(generateAdapter = true)
data class XtreamUserInfo(
    val username: String?,
    val password: String?,
    val message: String?,
    val auth: Int?,
    val status: String?,
    @Json(name = "exp_date") val expDate: String?,
    @Json(name = "is_trial") val isTrial: String?,
    @Json(name = "active_cons") val activeCons: String?,
    @Json(name = "created_at") val createdAt: String?,
    @Json(name = "max_connections") val maxConnections: String?
)

@JsonClass(generateAdapter = true)
data class XtreamCategory(
    @Json(name = "category_id") val categoryId: String,
    @Json(name = "category_name") val categoryName: String,
    @Json(name = "parent_id") val parentId: Int?
)

@JsonClass(generateAdapter = true)
data class XtreamStream(
    val num: Int?,
    val name: String,
    @Json(name = "stream_type") val streamType: String?,
    @Json(name = "stream_id") val streamId: Int,
    @Json(name = "stream_icon") val streamIcon: String?,
    @Json(name = "epg_channel_id") val epgChannelId: String?,
    @Json(name = "added") val added: String?,
    @Json(name = "category_id") val categoryId: String,
    @Json(name = "custom_sid") val customSid: String?,
    @Json(name = "tv_archive") val tvArchive: Int?,
    @Json(name = "direct_source") val directSource: String?,
    @Json(name = "tv_archive_duration") val tvArchiveDuration: Int?
)

interface XtreamApi {
    @GET
    suspend fun authenticate(
        @Url url: String,
        @Query("username") username: String,
        @Query("password") password: String
    ): XtreamUserInfoResponse

    @GET
    suspend fun getCategories(
        @Url url: String,
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("action") action: String = "get_live_categories"
    ): List<XtreamCategory>

    @GET
    suspend fun getStreams(
        @Url url: String,
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("action") action: String = "get_live_streams"
    ): List<XtreamStream>
}
