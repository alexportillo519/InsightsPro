package com.alexp.insightspro.networking

import retrofit2.Call
import retrofit2.http.*
import java.time.ZoneId

interface InstagramGraphsAPI {

    @GET("me/accounts?fields=connected_instagram_account")
    fun getInstagramAccountId(@Query("access_token") accessToken: String): Call<InstagramAccountId>

    @GET("{id}/media?fields=comments")
    fun getCommentDetails(@Path("id") id: String?, @Query("access_token") accessToken: String): Call<CommentsDetails>
}