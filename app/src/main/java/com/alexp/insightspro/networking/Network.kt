package com.alexp.insightspro.networking

import android.util.Log
import com.alexp.insightspro.models.Comment
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object Network {

    private var comment: Comment? = null
    private var instagramId: String? = null
    private var listOfComments = mutableListOf<Comment>()

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

    fun getInstagramAccountId(accessToken: String, onSuccess: (String?) -> Unit) {
        if(instagramId != null) {
            onSuccess(instagramId)
        }
        instagramGraphsAPI.getInstagramAccountId(accessToken).enqueue(InstagramIdCallback(onSuccess))
    }

    fun getCommentDetails(accessToken: String, onSuccess: (List<Comment>) -> Unit) {
        if (listOfComments?.isNotEmpty() == true) {
            onSuccess(listOfComments?.toList() ?: emptyList())
        }
        instagramGraphsAPI.getCommentDetails(instagramId, accessToken).enqueue(CommentsCallback(onSuccess))
    }

    private class CommentsCallback(private val onSuccess: (List<Comment>) -> Unit) : Callback<CommentsDetails> {
        override fun onResponse(call: Call<CommentsDetails>, response: Response<CommentsDetails>) {
            listOfComments = response.body()?.toDetails()!!
            Log.d("Network", "List of comments: $listOfComments")
            onSuccess(listOfComments?.toList() ?: emptyList())
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

    private fun CommentsDetails.toDetails(): MutableList<Comment> {

        this.listOfPostsWithComments.forEach {
            it.comments.data.forEach { commentInfo ->
                listOfComments.add(Comment(
                    timePosted = commentInfo.timePosted,
                    text = commentInfo.text,
                    commentId = commentInfo.commentId,
                    postId = it.igPostId
                ))
                Log.d("Network", "YAASSS ${commentInfo.text}")
            }
        }
        return listOfComments
    }
}