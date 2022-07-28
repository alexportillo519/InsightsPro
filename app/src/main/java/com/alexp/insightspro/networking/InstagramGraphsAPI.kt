package com.alexp.insightspro.networking

import retrofit2.Call
import retrofit2.http.*
import java.time.ZoneId

interface InstagramGraphsAPI {

    @GET("me/accounts?fields=connected_instagram_account")
    fun getInstagramAccountId(@Query("access_token") accessToken: String?): Call<InstagramAccountId>

    @GET("{id}/media?fields=comments{from,like_count,text,timestamp,replies{from,timestamp,text,like_count}}")
    fun getCommentDetails(
        @Path("id") accountId: String?,
        @Query("access_token") accessToken: String?
    ): Call<CommentsDetails>

    @GET("{id}?fields=replies{from,id,like_count,timestamp,text}")
    fun getReplies(
        @Path("id") commentId: String?,
        @Query("access_token") accessToken: String?
    ) : Call<GetReplies>

    @GET("{id}?fields=profile_picture_url,followers_count,follows_count,name,username,media_count,biography")
    fun getProfilePictureAndUsername(
        @Path("id") accountId: String?,
        @Query("access_token") accessToken: String?
    ): Call<ProfilePicAndUsername>

    @GET("{id}/insights?metric=impressions,profile_views,website_clicks,email_contacts,phone_call_clicks&period=day")
    fun getProfileInsights(
        @Path("id")accountId: String?,
        @Query("access_token") accessToken: String?
    ): Call<InsightsData>

    @GET("{id}?fields=media{media_url,caption,comments_count,like_count,media_type}")
    fun getPostData(
        @Path("id")accountId: String?,
        @Query("access_token") accessToken: String?
    ) : Call<PostData>

    @GET("{id}/children?fields=media_url")
    fun getCarouselImages(
        @Path("id") postId: String?,
        @Query("access_token") accessToken: String?
    ): Call<CarouselPostData>

    @DELETE("{id}")
    fun deleteComment(
        @Path("id") commentId: String?,
        @Query("access_token") accessToken: String?
    ): Call<DeleteResponse>

    @POST("{id}/replies")
    fun postReply(
        @Path("id") commentId: String?,
        @Query("message") message: String?,
        @Query("access_token") accessToken: String?
    ) : Call<PostingReplyResult>

}