package com.alexp.insightspro

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.alexp.insightspro.databinding.ActivityMainBinding
import com.alexp.insightspro.networking.Network
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger



class MainActivity : AppCompatActivity() {
    // Development Key Hash: VzSiQcXRmi2kyjzcA+mYLEtbGVs=

    private lateinit var binding: ActivityMainBinding
    private val callbackManager: CallbackManager = CallbackManager.Factory.create()
    private var accessToken: AccessToken? = null
    private var accessTokenTracker: AccessTokenTracker? = null
    private val permissions = listOf("email", "public_profile", "pages_show_list", "ads_management", "business_management", "instagram_basic",  "instagram_manage_comments", "pages_read_engagement")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        FacebookSdk.fullyInitialize()
        AppEventsLogger.activateApp(application)

        val isLoggedIn = accessToken != null && !accessToken!!.isExpired
        binding.loginButton.setPermissions(permissions)

        binding.loginButton.setOnClickListener {
            binding.loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onCancel() {
                    Log.d("MainActivity", "Log in was canceled.")
                }

                override fun onError(error: FacebookException) {
                    Log.e("MainActivity", "Error: $error")
                }

                override fun onSuccess(result: LoginResult) {
                    accessToken = AccessToken.getCurrentAccessToken()
                    Log.d("MainActivity", "Access Token: ${accessToken?.token} ${accessToken?.permissions}")
                    Network.getInstagramAccountId(accessToken?.token ?: "") { instagramID ->
                        Log.d("MainActivity", "Instagram Account ID: $instagramID")
                        if (it != null) {
                            Network.getCommentDetails(accessToken?.token ?: "") { comments ->
                                Log.d("MainActivity", "Comments: $comments")
                            }
                        }
                    }
                }
            })

        }

        setContentView(binding.root)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        accessTokenTracker?.stopTracking()
    }
}