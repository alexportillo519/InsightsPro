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
        mainViewModel.setAccessToken(accessToken?.token)
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        setOnClickListeners()

        setObservers()

        return binding.root
    }

    private fun setOnClickListeners() {
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

        binding.myProfileCardView.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_myProfileFragment)
        }

        binding.postsCardView.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_postsFragment)
        }

        binding.flagsCardView.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_flaggedCommentsFragment)
        }
    }

    private fun setObservers() {
        mainViewModel.accessToken.observe(viewLifecycleOwner) { token ->
            Network.getInstagramAccountId(token ?: "") { id ->

                if (id == null) {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Connect Your Instagram Account")
                        .setMessage("Make sure your instagram is either a business or a creator account. Then create a Facebook Page and connect that to your Instagram Account.")
                        .setPositiveButton("Steps") { dialog, _ ->
                            dialog.cancel()
                            AlertDialog.Builder(requireContext())
                                .setTitle("Connect Your Instagram Account")
                                .setMessage("Step 1: Create a Facebook Page\n" +
                                        "Sign in to Facebook -> Home -> Pages -> Create New Page\n" +
                                        "Step 2: Connect your Facebook Page to your Instagram\n" +
                                        "Go to your Facebook Page you just created -> Settings -> Instagram -> Connect Account\n" +
                                        "You should be set!")
                                .setPositiveButton("OK") { dialog2, _ ->
                                    dialog2.cancel()
                                }
                                .create().show()
                        }
                        .setNegativeButton("Cancel") { dialog, _ ->
                            dialog.cancel()
                        }
                        .create().show()
                }

                Network.getProfilePicture(id,token ?:"") { accountData ->
                    Glide.with(this).load(accountData?.profilePic).into(binding.profilePicIV)
                    binding.usernameTv.text = accountData?.username
                }

            }
        }

    }

}