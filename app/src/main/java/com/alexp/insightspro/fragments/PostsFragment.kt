package com.alexp.insightspro.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.alexp.insightspro.MainViewModel
import com.alexp.insightspro.R
import com.alexp.insightspro.adapter.PostAdapter
import com.alexp.insightspro.databinding.FragmentPostsBinding
import com.alexp.insightspro.models.Post
import com.alexp.insightspro.networking.Network


class PostsFragment : Fragment() {

    private lateinit var binding: FragmentPostsBinding
    private val postAdapter = PostAdapter() { postClicked ->
        onPostClicked(postClicked)
    }
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostsBinding.inflate(inflater, container, false)
        binding.circularProgressBar.visibility = View.VISIBLE

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
            binding.circularProgressBar.visibility = View.INVISIBLE
            binding.postsRecyclerview.adapter = postAdapter
        }
    }

    private fun onPostClicked(postClicked: Post) {
        mainViewModel.setPostClicked(postClicked)
        findNavController().navigate(R.id.action_postsFragment_to_postDetailsFragment)
    }

}