package com.alexp.insightspro.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alexp.insightspro.MainViewModel
import com.alexp.insightspro.R
import com.alexp.insightspro.adapter.CommentAdapter
import com.alexp.insightspro.databinding.FragmentAllCommentsBinding
import com.alexp.insightspro.models.Comment
import com.alexp.insightspro.networking.Network
import com.facebook.FacebookSdk


class AllCommentsFragment : Fragment() {

    private lateinit var binding: FragmentAllCommentsBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private val commentAdapter = CommentAdapter { commentChosen ->
        onCommentChosen(commentChosen)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllCommentsBinding.inflate(layoutInflater, container, false)
        FacebookSdk.fullyInitialize()
        binding.circularProgressBar.visibility = View.VISIBLE

        binding.commentRecyclerView.apply {
            adapter = commentAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        setObservers()

        return binding.root
    }

    private fun onCommentChosen(comment: Comment) {
        mainViewModel.setCommentClicked(comment)
        findNavController().navigate(R.id.action_allCommentsFragment_to_commentDetailsFragment)
    }

    private fun setObservers() {
        mainViewModel.accessToken.observe(viewLifecycleOwner) { token ->

            Network.getCommentDetails(token ?: "") { comments ->
                mainViewModel.setComments(comments)
            }

        }

        mainViewModel.listOfComments.observe(viewLifecycleOwner) {
            commentAdapter.submitList(it)
            binding.circularProgressBar.visibility = View.INVISIBLE
            binding.commentRecyclerView.adapter = commentAdapter
        }
    }

}