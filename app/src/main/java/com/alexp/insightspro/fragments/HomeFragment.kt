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
                .setTitle(R.string.log_out)
                .setMessage(R.string.are_you_sure_you_want_to_log_out)
                .setPositiveButton(R.string.yes) { _, _ ->
                    AccessToken.setCurrentAccessToken(null)
                    LoginManager.getInstance().logOut()
                    findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
                }
                .setNegativeButton(R.string.no) { _, _ -> }
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
                        .setTitle(R.string.connect_your_instagram_account)
                        .setMessage(R.string.make_sure_account_is)
                        .setPositiveButton(R.string.steps) { dialog, _ ->
                            dialog.cancel()
                            AlertDialog.Builder(requireContext())
                                .setTitle(R.string.connect_your_instagram_account)
                                .setMessage(R.string.steps_on_how_to_set_up)
                                .setPositiveButton(R.string.ok) { dialog2, _ ->
                                    dialog2.cancel()
                                }
                                .create().show()
                        }
                        .setNegativeButton(R.string.cancel) { dialog, _ ->
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