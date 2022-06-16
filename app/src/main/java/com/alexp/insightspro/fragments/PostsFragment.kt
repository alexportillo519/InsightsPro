package com.alexp.insightspro.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.alexp.insightspro.MainViewModel
import com.alexp.insightspro.adapter.PostAdapter
import com.alexp.insightspro.databinding.FragmentPostsBinding
import com.alexp.insightspro.networking.Network

class PostsFragment : Fragment() {

    private lateinit var binding: FragmentPostsBinding
    private val postAdapter = PostAdapter()
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostsBinding.inflate(inflater, container, false)

        setObservers()

        binding.postsRecyclerview.apply {
            adapter = postAdapter
        }


        return binding.root
    }

    private fun setObservers() {
        mainViewModel.accessToken.observe(viewLifecycleOwner) { accessToken ->

            Network.getInstagramAccountId(accessToken) { accountId ->
                Network.getPostData(accountId, accessToken) { listOfPostData ->
                    mainViewModel.setPosts(listOfPostData)
                }
            }

        }

        mainViewModel.listOfPosts.observe(viewLifecycleOwner) { posts->
            postAdapter.submitList(posts)
            binding.postsRecyclerview.adapter = postAdapter
        }
    }

}