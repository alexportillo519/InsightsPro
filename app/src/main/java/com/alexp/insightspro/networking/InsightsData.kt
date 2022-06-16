package com.alexp.insightspro.networking

import android.provider.MediaStore
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class InsightsData(
    @Json(name = "data")val data: List<Data>
)

@JsonClass(generateAdapter = true)
data class Data(
    @Json(name = "title")val title: String,
    @Json(name = "values")val listOfValues: List<Values>
)

@JsonClass(generateAdapter = true)
data class Values(
    @Json(name = "value")val value: Int
)

@JsonClass(generateAdapter = true)
data class PostData(
    @Json(name = "media")val media: Media
)

@JsonClass(generateAdapter = true)
data class Media(
    @Json(name = "data") val listOfData: List<PostInfo>
)

@JsonClass(generateAdapter = true)
data class PostInfo(
    @Json(name = "media_url")val postUrl: String,
    @Json(name = "caption")val caption: String,
    @Json(name = "comments_count")val commentsCount: Int,
    @Json(name = "like_count")val likeCount: Int,
    @Json(name = "id") val postId: String
)