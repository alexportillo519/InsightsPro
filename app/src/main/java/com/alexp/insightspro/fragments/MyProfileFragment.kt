package com.alexp.insightspro.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.alexp.insightspro.MainViewModel
import com.alexp.insightspro.R
import com.alexp.insightspro.databinding.FragmentMyProfileBinding
import com.alexp.insightspro.networking.Network
import com.bumptech.glide.Glide

class MyProfileFragment : Fragment() {

    private lateinit var binding: FragmentMyProfileBinding
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyProfileBinding.inflate(inflater, container, false)

        setUpObservers()

        return binding.root
    }

    private fun setUpObservers() {
        mainViewModel.accessToken.observe(viewLifecycleOwner) { token ->
            Network.getInstagramAccountId(token ?: "") { id ->

                Network.getProfilePicture(id,token ?:"") { accountData ->
                    Glide.with(this).load(accountData?.profilePic).into(binding.profilePicIV)
                    binding.amountOfPostsTV.text = accountData?.numOfPosts.toString()
                    binding.amountOfFollowersTV.text = accountData?.followerCount.toString()
                    binding.amountOfFollowingTV.text = accountData?.followingCount.toString()
                    binding.nameTV.text = accountData?.name
                    binding.biographyTV.text = accountData?.bio
                }

                Network.getAccountInsights(id, token ?: "") { insights ->
                    binding.amountOfProfileVisits.text = insights?.profileVisits.toString()
                    binding.amountOfPostsViews.text = insights?.postViews.toString()
                    binding.amountOfEmailClicks.text = insights?.emailClicks.toString()
                    binding.amountOfPhoneClicks.text = insights?.phoneClicks.toString()
                    binding.amountOfWebsiteClicks.text = insights?.websiteClicks.toString()
                }

            }
        }
    }

}