package com.alexp.insightspro.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
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
import com.facebook.AccessToken
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager


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


        mainViewModel.accessToken.observe(viewLifecycleOwner) { token ->

            Network.getCommentDetails(token ?: "") { comments ->
                mainViewModel.setComments(comments)
            }

        }

        mainViewModel.listOfComments.observe(viewLifecycleOwner) {
            commentAdapter.submitList(it)
            binding.commentRecyclerView.adapter = commentAdapter
        }

        binding.commentRecyclerView.apply {
            adapter = commentAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

//        setHasOptionsMenu(true)

        return binding.root
    }

    private fun onCommentChosen(comment: Comment) {
        mainViewModel.setCommentClicked(comment)
        findNavController().navigate(R.id.action_allCommentsFragment_to_commentDetailsFragment)
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.home_menu, menu)
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == R.id.login_menu) {
//            AlertDialog.Builder(requireContext())
//                .setTitle("Log Out?")
//                .setMessage("Are you sure you want to log out?")
//                .setPositiveButton("Yes") { _, _ ->
//                    AccessToken.setCurrentAccessToken(null)
//                    LoginManager.getInstance().logOut()
//                    findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
//                }
//                .setNegativeButton("No") { _, _ -> }
//                .create().show()
//        }
//        return super.onOptionsItemSelected(item)
//    }

}