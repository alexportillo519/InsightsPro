package com.alexp.insightspro.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.alexp.insightspro.MainViewModel
import com.alexp.insightspro.R
import com.alexp.insightspro.databinding.FragmentLoginBinding
import com.alexp.insightspro.networking.Network
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginResult

class LoginFragment : Fragment() {

    private var binding: FragmentLoginBinding? = null
    private val mainViewModel: MainViewModel by activityViewModels()
    private val callbackManager: CallbackManager = CallbackManager.Factory.create()
    private var accessToken: AccessToken? = null
    private var accessTokenTracker: AccessTokenTracker? = null
    private val permissions = listOf("email", "public_profile", "pages_show_list", "ads_management", "business_management", "instagram_basic",  "instagram_manage_comments", "instagram_manage_insights", "pages_read_engagement", "read_insights")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        FacebookSdk.fullyInitialize()
        AppEventsLogger.activateApp(requireActivity().application)

        checkLoginStatus()

        binding?.loginButton?.setOnClickListener {
            binding?.loginButton?.registerCallback(callbackManager, object :
                FacebookCallback<LoginResult> {
                override fun onCancel() {
                    Log.d("MainActivity", "Log in was canceled.")
                }

                override fun onError(error: FacebookException) {
                    Log.e("MainActivity", "Error: $error")
                }

                override fun onSuccess(result: LoginResult) {
                    accessToken = AccessToken.getCurrentAccessToken()
                    mainViewModel.setAccessToken(accessToken?.token)
                    Log.d("LoginFragment", "Access Token: $accessToken")
                    changeFragment()
                }
            })
        }
        return binding?.root
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        accessTokenTracker?.stopTracking()
    }

    private fun checkLoginStatus() {
        accessToken = AccessToken.getCurrentAccessToken()
        mainViewModel.setAccessToken(accessToken?.token)
        Log.d("LoginFragment", "Access Token ViewModel: ${mainViewModel.accessToken.value}")
        val isLoggedIn = accessToken != null && !accessToken!!.isExpired
        if (isLoggedIn && mainViewModel.isFirstTimeLoggedIn) {
            mainViewModel.isFirstTimeLoggedIn = false
            changeFragment()
        }
        binding?.loginButton?.setPermissions(permissions)
    }

    fun changeFragment() {
        findNavController().navigate(R.id.action_loginFragment_to_homeFragment2)
    }
}