package com.alexp.insightspro.models

data class Comment (
    val timePosted: String,
    val text: String,
    val commentId: String,
    val postId: String
)