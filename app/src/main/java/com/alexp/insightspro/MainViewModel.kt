package com.alexp.insightspro


import android.content.Context
import android.content.res.Resources
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alexp.insightspro.models.Comment
import com.alexp.insightspro.models.Post

class MainViewModel : ViewModel() {

    private val _listOfComments = MutableLiveData<List<Comment>>()
    val listOfComments: LiveData<List<Comment>> = _listOfComments

    private val _listOfFlaggedComments = MutableLiveData<List<Comment>>()
    val listOfFlaggedComments: LiveData<List<Comment>> = _listOfFlaggedComments

    private val _listOfPosts = MutableLiveData<List<Post?>>()
    val listOfPosts: LiveData<List<Post?>> = _listOfPosts

    var isFirstTimeLoggedIn: Boolean = true

    var accessToken = MutableLiveData<String?>()

    var commentClicked = MutableLiveData<Comment>()

    private var tempListOfPosts = mutableListOf<Post?>()

    private val listOfFlaggedWords = listOf("wtf","fuck","arse","crap","arsehole","ass","asshole","bitch","bullshit","shit","tits","bastard","cock","dick","prick","pussy","twat","motherfucker","slut","piss")

    fun setComments(comments: List<Comment>) {

        _listOfComments.value = comments.toMutableList()

        val flaggedComments = mutableListOf<Comment>()
        comments.forEach { comment ->
            filterComments(comment, flaggedComments)
        }

        comments.forEach { comment ->
            filterReplies(comment, flaggedComments)
        }

        _listOfFlaggedComments.value = flaggedComments
    }

    fun setCommentClicked(comment: Comment) {
        commentClicked.value = comment
    }

    fun setAccessToken(token: String?) {
        accessToken.value = token
    }

    fun setPosts(posts: List<Post?>) {
        tempListOfPosts.clear()
        tempListOfPosts.addAll(posts)
        _listOfPosts.value = tempListOfPosts
    }

    private fun filterComments(comment: Comment, flaggedComments: MutableList<Comment>) {
        listOfFlaggedWords.filter {
            if(comment.text?.contains(it, true) == true) {
                flaggedComments.add(comment)
            } else {
                false
            }
        }
    }

    private fun filterReplies(comment: Comment, flaggedComments: MutableList<Comment>) {
        comment.replies?.forEach { reply ->
            listOfFlaggedWords.filter {
                if(reply.text?.contains(it, true) == true) {
                    if (!flaggedComments.contains(comment)) {
                        flaggedComments.add(comment)
                    }
                    true
                } else {
                    false
                }
            }
        }
    }

}