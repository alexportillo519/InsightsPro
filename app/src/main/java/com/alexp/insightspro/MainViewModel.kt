package com.alexp.insightspro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alexp.insightspro.models.Comment
import com.facebook.AccessToken

class MainViewModel : ViewModel() {

    var listOfComments = MutableLiveData<MutableList<Comment>>()

    var isFirstTimeLoggedIn: Boolean = true

    var accessToken = MutableLiveData<String?>()

    var commentClicked = MutableLiveData<Comment>()

    private var tempListOfComments = mutableListOf<Comment>()

    var profilePictureUrl = MutableLiveData<String?>()

    var username = MutableLiveData<String?>()

    fun setComments(comments: List<Comment>) {
        tempListOfComments.clear()
        tempListOfComments.addAll(comments)
        listOfComments.value = tempListOfComments
    }

    fun setCommentClicked(comment: Comment) {
        commentClicked.value = comment
    }

    fun setAccessToken(token: String?) {
        accessToken.value = token
    }

    fun setProfilePictureUrl(url: String?) {
        profilePictureUrl.value = url
    }

    fun setUsername(userName : String?) {
        username.value = userName
    }
}