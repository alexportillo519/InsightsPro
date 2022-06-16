package com.alexp.insightspro


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alexp.insightspro.models.Comment
import com.alexp.insightspro.models.Post

class MainViewModel : ViewModel() {

    var listOfComments = MutableLiveData<MutableList<Comment>>()
    var listOfFlaggedComments = MutableLiveData<MutableList<Comment>>()

    var isFirstTimeLoggedIn: Boolean = true

    var accessToken = MutableLiveData<String?>()

    var commentClicked = MutableLiveData<Comment>()

    private var tempListOfComments = mutableListOf<Comment>()

    var listOfPosts = MutableLiveData<MutableList<Post?>>()

    private var tempListOfPosts = mutableListOf<Post?>()

    fun setComments(comments: List<Comment>) {
        tempListOfComments.clear()
        tempListOfComments.addAll(comments)
        listOfComments.value = tempListOfComments

        // add more offensive words
        val keywords = arrayOf("wtf")
        val flaggedComments = mutableListOf<Comment>()
        comments.forEach { comment ->
            keywords.filter {
                if(comment.text?.contains(it, true) == true) {
                    flaggedComments.add(comment)
                } else {
                    false
                }
            }
        }
        comments.forEach { comment ->
            comment.replies?.forEach { reply ->
                keywords.filter {
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
        listOfFlaggedComments.value = flaggedComments

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
        listOfPosts.value = tempListOfPosts
    }
}