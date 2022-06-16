package com.alexp.insightspro.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputType
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alexp.insightspro.MainViewModel
import com.alexp.insightspro.R
import com.alexp.insightspro.adapter.ReplyAdapter
import com.alexp.insightspro.databinding.FragmentCommentDetailsBinding
import com.alexp.insightspro.models.Reply
import com.alexp.insightspro.networking.Network
import com.facebook.AccessToken
import com.facebook.FacebookSdk
import java.text.SimpleDateFormat
import java.util.*


class CommentDetailsFragment : Fragment() {

    private lateinit var binding: FragmentCommentDetailsBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private val replyAdapter = ReplyAdapter { replyClicked ->
        deleteReply(replyClicked)
    }
    private var accessToken = AccessToken.getCurrentAccessToken()
    private var commentClickedId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommentDetailsBinding.inflate(layoutInflater, container, false)
        FacebookSdk.fullyInitialize()

        setUpAllViews()

        return binding.root
    }

    private fun setUpAllViews() {
        mainViewModel.commentClicked.observe(viewLifecycleOwner) { comment ->
            commentClickedId = comment.commentId
            binding.commentUsernameTV.text = comment.userWhoCommented
            binding.commentTextTV.text = comment.text
            binding.commentLikesTV.text = getString(R.string.like_count, comment.likeCount)
            binding.commentTimePostedTV.text = formatTimePosted(comment.timePosted)
            replyAdapter.submitList(comment.replies ?: emptyList())
            binding.repliesRecyclerView.adapter = replyAdapter
            binding.repliesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.deleteIv.setOnClickListener {
                setUpAlertDialog(comment.commentId)
            }
            binding.commentReplyTV.setOnClickListener {
                setUpReplyAlertDialog(comment.commentId)
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun formatTimePosted(timeFromJson: String?): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
        sdf.timeZone = TimeZone.getTimeZone("GMT")
        val time = sdf.parse(timeFromJson)?.time
        val now = System.currentTimeMillis()
        return DateUtils.getRelativeTimeSpanString(time!!, now, DateUtils.MINUTE_IN_MILLIS).toString()
    }

    private fun setUpAlertDialog(commentId: String?) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Comment?")
            .setMessage("Are you sure you want to delete this comment?")
            .setPositiveButton("Yes") { _, _ ->
                binding.circularProgressBar.visibility = View.VISIBLE
                mainViewModel.setAccessToken(accessToken?.token)
                mainViewModel.accessToken.observe(viewLifecycleOwner) { accessToken ->
                    Network.deleteComment(commentId, accessToken) { wasCommentDeleted ->
                        if(wasCommentDeleted == true) {
                            Network.getCommentDetails(accessToken) { comments ->
                                binding.circularProgressBar.visibility = View.INVISIBLE
                                mainViewModel.setComments(comments)
                            }
                            findNavController().navigateUp()
                        }
                    }
                }
            }
            .setNegativeButton("No") { _, _ -> }
            .create().show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUpReplyAlertDialog(commentId: String?) {

        val input = EditText(requireContext())
        input.hint = "Enter Reply"
        input.inputType = InputType.TYPE_CLASS_TEXT
        input.setSingleLine()
        val container = FrameLayout(requireContext())
        val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.leftMargin = resources.getDimensionPixelSize(R.dimen.dialog_margin)
        params.rightMargin = resources.getDimensionPixelSize(R.dimen.dialog_margin)
        input.layoutParams = params
        container.addView(input)


        AlertDialog.Builder(requireContext())
            .setTitle("Reply")
            .setView(container)
            .setPositiveButton("Post") { _, _ ->
                binding.circularProgressBar.visibility = View.VISIBLE
                val inputText = input.text.toString()
                Network.postReply(commentId, accessToken?.token, inputText){ replyWasPosted ->
                    if (replyWasPosted == true) {
                        Network.getReplies(commentId, accessToken?.token) { replies ->
                            binding.circularProgressBar.visibility = View.INVISIBLE
                            replyAdapter.submitList(replies)
                            binding.repliesRecyclerView.adapter?.notifyDataSetChanged()
                        }
                    }
                }

            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .create().show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun deleteReply(reply: Reply) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Reply?")
            .setMessage("Are you sure you want to delete this reply?")
            .setPositiveButton("Yes") { _, _ ->
                binding.circularProgressBar.visibility = View.VISIBLE
                mainViewModel.setAccessToken(accessToken?.token)
                mainViewModel.accessToken.observe(viewLifecycleOwner) { accessToken ->
                    Network.deleteComment(reply.replyId, accessToken) { wasCommentDeleted ->
                        if(wasCommentDeleted == true) {
                            Network.getReplies(commentClickedId, accessToken) { replies ->
                                binding.circularProgressBar.visibility = View.INVISIBLE
                                replyAdapter.submitList(replies)
                                binding.repliesRecyclerView.adapter?.notifyDataSetChanged()
                            }
                        }
                    }
                }
            }
            .setNegativeButton("No") { _, _ -> }
            .create().show()
    }
}