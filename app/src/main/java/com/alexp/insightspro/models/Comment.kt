package com.alexp.insightspro.models

data class Comment (
    val timePosted: String?,
    val text: String?,
    val commentId: String?,
    val postId: String?,
    val likeCount: Int?,
    val userWhoCommented: String?,
    val replies: List<Reply>?
)

data class Reply(
    val userWhoCommented: String?,
    val timePosted: String?,
    val text: String?,
    val replyId: String?,
    val likeCount: Int?
)