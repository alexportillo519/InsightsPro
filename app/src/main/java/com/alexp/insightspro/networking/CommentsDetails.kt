package com.alexp.insightspro.networking

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommentsDetails(
    @Json(name = "data")val listOfPostsWithComments: List<Comments>?
)

@JsonClass(generateAdapter = true)
data class Comments(
    @Json(name = "comments")val comments: CommentsData?,
    @Json(name = "id")val igPostId: String?
)

@JsonClass(generateAdapter = true)
data class CommentsData(
    @Json(name = "data")val data: List<CommentsInformation>?
)

@JsonClass(generateAdapter = true)
data class CommentsInformation(
    @Json(name = "timestamp")val timePosted: String?,
    @Json(name = "text")val text: String?,
    @Json(name = "id")val commentId: String?,
    @Json(name = "like_count")val likeCount: Int?,
    @Json(name = "from")val userWhoCommented: UserWhoCommented?,
    @Json(name = "replies")val replies: ReplyData?
)

@JsonClass(generateAdapter = true)
data class UserWhoCommented(
    @Json(name = "username")val username: String?
)

@JsonClass(generateAdapter = true)
data class GetReplies(
    @Json(name = "replies") val replies: ReplyData?
)

@JsonClass(generateAdapter = true)
data class ReplyData(
    @Json(name = "data")val listOfReplies: List<RepliesData>?
)

@JsonClass(generateAdapter = true)
data class RepliesData(
    @Json(name = "from")val userWhoCommented: UserWhoCommented?,
    @Json(name = "timestamp")val timePosted: String?,
    @Json(name = "text")val text: String?,
    @Json(name = "id")val replyId: String?,
    @Json(name = "like_count")val likeCount: Int?
)

@JsonClass(generateAdapter = true)
data class DeleteResponse(
    @Json(name = "success") val isSuccessful: Boolean
)

@JsonClass(generateAdapter = true)
data class PostingReplyResult(
    @Json(name = "id")val replyId: String?
)


