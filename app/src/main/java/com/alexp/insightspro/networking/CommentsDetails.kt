package com.alexp.insightspro.networking

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommentsDetails(
    @Json(name = "data")val listOfPostsWithComments: List<Comments>
)

@JsonClass(generateAdapter = true)
data class Comments(
    @Json(name = "comments")val comments: CommentsData,
    @Json(name = "id")val igPostId: String
)

@JsonClass(generateAdapter = true)
data class CommentsData(
    @Json(name = "data")val data: List<CommentsInformation>
)

@JsonClass(generateAdapter = true)
data class CommentsInformation(
    @Json(name = "timestamp")val timePosted: String,
    @Json(name = "text")val text: String,
    @Json(name = "id")val commentId: String
)
