package com.alexp.insightspro.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.alexp.insightspro.MainViewModel
import com.alexp.insightspro.R
import com.alexp.insightspro.databinding.FragmentHomeBinding
import com.alexp.insightspro.networking.Network
import com.bumptech.glide.Glide
import com.facebook.AccessToken
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private var accessToken = AccessToken.getCurrentAccessToken()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        FacebookSdk.fullyInitialize()

        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        mainViewModel.setAccessToken(accessToken?.token)
        mainViewModel.accessToken.observe(viewLifecycleOwner) { token ->
            Network.getInstagramAccountId(token ?: "") { id ->

                Network.getProfilePicture(id,token ?:"") { url, username ->
                    mainViewModel.setProfilePictureUrl(url)
                    mainViewModel.setUsername(username)
                }

            }
        }

        binding.commentCardView.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_allCommentsFragment)
        }

        binding.logoutCardView.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Log Out?")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes") { _, _ ->
                    AccessToken.setCurrentAccessToken(null)
                    LoginManager.getInstance().logOut()
                    findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
                }
                .setNegativeButton("No") { _, _ -> }
                .create().show()
        }

        mainViewModel.profilePictureUrl.observe(viewLifecycleOwner) { url ->
            Glide.with(this).load(url).into(binding.profilePicIV)
        }

        mainViewModel.username.observe(viewLifecycleOwner) { username ->
            binding.usernameTv.text = username
        }

        return binding.root
    }

}