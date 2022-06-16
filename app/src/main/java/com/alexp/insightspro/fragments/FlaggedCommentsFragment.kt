package com.alexp.insightspro.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alexp.insightspro.MainViewModel
import com.alexp.insightspro.R
import com.alexp.insightspro.adapter.CommentAdapter
import com.alexp.insightspro.databinding.FragmentFlaggedCommentsBinding
import com.alexp.insightspro.models.Comment
import com.alexp.insightspro.networking.Network

class FlaggedCommentsFragment : Fragment() {

    private lateinit var binding: FragmentFlaggedCommentsBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private val commentAdapter = CommentAdapter { commentChosen ->
        onCommentChosen(commentChosen)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFlaggedCommentsBinding.inflate(inflater, container, false)

        binding.flaggedCommentRV.apply {
            adapter = commentAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        setObservers()

        return binding.root
    }

    private fun onCommentChosen(commentChosen: Comment) {
        mainViewModel.setCommentClicked(commentChosen)
        findNavController().navigate(R.id.action_flaggedCommentsFragment_to_commentDetailsFragment)
    }

    private fun setObservers() {
        mainViewModel.accessToken.observe(viewLifecycleOwner) { token ->

            Network.getCommentDetails(token ?: "") { comments ->
                mainViewModel.setComments(comments)
            }
        }

        mainViewModel.listOfFlaggedComments.observe(viewLifecycleOwner) { comments ->
            commentAdapter.submitList(comments)
            binding.flaggedCommentRV.adapter = commentAdapter
        }
    }

}