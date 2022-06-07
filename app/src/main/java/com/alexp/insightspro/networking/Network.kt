package com.alexp.insightspro.networking

import android.util.Log
import com.alexp.insightspro.models.Comment
import com.alexp.insightspro.models.Reply
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object Network {

    private var instagramId: String? = null
    private var listOfComments = mutableListOf<Comment>()
    private var listOfReplies = mutableListOf<Reply>()
    private var profilePictureUrl: String? = null
    private var username: String? = null
    private var wasCommentDeleted: Boolean? = null
    private var wasReplyPosted: Boolean? = null

    private val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
    private val client = OkHttpClient.Builder().addInterceptor(logger)
    private val instagramGraphsAPI: InstagramGraphsAPI
        get() {
            return Retrofit.Builder()
                .baseUrl("https://graph.facebook.com")
                .client(client.build())
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(InstagramGraphsAPI::class.java)
        }

    fun getInstagramAccountId(accessToken: String?, onSuccess: (String?) -> Unit) {
        if(instagramId != null) {
            onSuccess(instagramId)
        }
        instagramGraphsAPI.getInstagramAccountId(accessToken).enqueue(InstagramIdCallback(onSuccess))
    }

    fun getCommentDetails(accessToken: String?, onSuccess: (List<Comment>) -> Unit) {
        if (listOfComments.isNotEmpty()) {
            onSuccess(listOfComments.toList())
        }
        instagramGraphsAPI.getCommentDetails(instagramId, accessToken).enqueue(CommentsCallback(onSuccess))
    }

    fun getProfilePicture(id: String?, accessToken: String?, onSuccess: (String?, String?) -> Unit) {
        if(profilePictureUrl != null && username != null) {
            onSuccess(profilePictureUrl, username)
        }
        instagramGraphsAPI.getProfilePictureAndUsername(id, accessToken).enqueue(ProfilePictureCallback(onSuccess))
    }

    fun getReplies(replyId: String?, accessToken: String?, onSuccess: (List<Reply?>) -> Unit) {
        if (listOfReplies.isNotEmpty()) {
            onSuccess(listOfReplies)
        }
        instagramGraphsAPI.getReplies(replyId, accessToken).enqueue(ReplyCallback(onSuccess))
    }

    fun deleteComment(id: String?, accessToken: String?, onSuccess: (Boolean?) -> Unit) {
        instagramGraphsAPI.deleteComment(id, accessToken).enqueue(DeleteCallback(onSuccess))
    }

    fun postReply(id: String?, accessToken: String?, message: String?, onSuccess: (Boolean?) -> Unit) {
        if (wasReplyPosted != null) {
            onSuccess(wasReplyPosted)
        }
        instagramGraphsAPI.postReply(id, message, accessToken).enqueue(PostReplyCallback(onSuccess))
    }

    private class CommentsCallback(private val onSuccess: (List<Comment>) -> Unit) : Callback<CommentsDetails> {
        override fun onResponse(call: Call<CommentsDetails>, response: Response<CommentsDetails>) {
            listOfComments = response.body()?.toDetails()!!
            Log.d("Network", "List of comments: $listOfComments")
            onSuccess(listOfComments.toList())
        }

        override fun onFailure(call: Call<CommentsDetails>, t: Throwable) {
            Log.v("Networking", "Error! $t Instagram Account ID: $instagramId")
        }

    }

    private class InstagramIdCallback(private val onSuccess: (String?) -> Unit) : Callback<InstagramAccountId> {
        override fun onResponse(
            call: Call<InstagramAccountId>,
            response: Response<InstagramAccountId>
        ) {
            instagramId = response.body()?.data?.get(0)?.instagramId?.instagramId
            Log.d("Network", "Instagram Account ID: $instagramId")
            onSuccess(instagramId)
        }

        override fun onFailure(call: Call<InstagramAccountId>, t: Throwable) {
            Log.e("Networking", "Error! $t")
        }

    }

    private class ProfilePictureCallback(private val onSuccess: (String?, String?) -> Unit) : Callback<ProfilePicAndUsername> {
        override fun onResponse(
            call: Call<ProfilePicAndUsername>,
            response: Response<ProfilePicAndUsername>
        ) {
            profilePictureUrl = response.body()?.profilePic
            username = response.body()?.username
            onSuccess(profilePictureUrl, username)
        }

        override fun onFailure(call: Call<ProfilePicAndUsername>, t: Throwable) {
            Log.e("Networking", "Error! $t")
        }

    }

    private class DeleteCallback(private val onSuccess: (Boolean?) -> Unit) : Callback<DeleteResponse> {
        override fun onResponse(call: Call<DeleteResponse>, response: Response<DeleteResponse>) {
            wasCommentDeleted = response.body()?.isSuccessful
            onSuccess(wasCommentDeleted)
        }

        override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {
            Log.e("Networking", "Error! $t")
        }

    }

    private class PostReplyCallback(private val onSuccess: (Boolean?) -> Unit) : Callback<PostingReplyResult> {
        override fun onResponse(
            call: Call<PostingReplyResult>,
            response: Response<PostingReplyResult>
        ) {
            onSuccess(true)
        }

        override fun onFailure(call: Call<PostingReplyResult>, t: Throwable) {
            onSuccess(false)
            Log.e("Networking", "Error! $t")
        }

    }

    private class ReplyCallback(private val onSuccess: (List<Reply?>) -> Unit) : Callback<GetReplies> {
        override fun onResponse(call: Call<GetReplies>, response: Response<GetReplies>) {
            listOfReplies = response.body()?.replies?.listOfReplies?.toReplies()?.toMutableList() ?: emptyList<Reply>().toMutableList()
            onSuccess(listOfReplies)
        }

        override fun onFailure(call: Call<GetReplies>, t: Throwable) {
            Log.e("Networking", "Error! $t")
        }

    }

    private fun CommentsDetails.toDetails(): MutableList<Comment> {

        listOfComments.clear()

        this.listOfPostsWithComments?.forEach {
            it.comments?.data?.forEach { commentInfo ->
                listOfComments.add(Comment(
                    timePosted = commentInfo.timePosted,
                    text = commentInfo.text,
                    commentId = commentInfo.commentId,
                    postId = it.igPostId,
                    likeCount = commentInfo.likeCount,
                    userWhoCommented = commentInfo.userWhoCommented?.username,
                    replies = commentInfo.replies?.listOfReplies?.toReplies()
                ))
            }
        }
        return listOfComments
    }

    private fun List<RepliesData>.toReplies() : List<Reply> {
        val list = mutableListOf<Reply>()
        this.forEach { repliesData ->
            list.add(Reply(
                repliesData.userWhoCommented?.username,
                repliesData.timePosted,
                repliesData.text,
                repliesData.replyId,
                repliesData.likeCount
            ))
        }
        return list
    }
}