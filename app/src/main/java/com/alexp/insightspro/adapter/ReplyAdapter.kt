package com.alexp.insightspro.adapter


import android.annotation.SuppressLint
import android.content.res.Resources
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alexp.insightspro.R
import com.alexp.insightspro.databinding.RepliesBinding
import com.alexp.insightspro.models.Reply
import com.alexp.insightspro.networking.Network
import com.alexp.insightspro.utils.DateUtil
import java.text.SimpleDateFormat
import java.util.*

class ReplyAdapter(
    private val onReplyClicked: (reply: Reply) -> Unit
) : ListAdapter<Reply, ReplyAdapter.ItemViewHolder>(diff) {

    companion object {
        val diff = object : DiffUtil.ItemCallback<Reply>() {
            override fun areItemsTheSame(oldItem: Reply, newItem: Reply): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Reply, newItem: Reply): Boolean {
                return oldItem == newItem
            }
        }
    }

    class ItemViewHolder(
        private val binding: RepliesBinding,
        private val onReplyClicked: (reply: Reply) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(reply: Reply) {
            binding.apply {
                replyUsernameTV.text = reply.userWhoCommented
                replyTextTV.text = reply.text
                replyTimePostedTV.text = DateUtil().formatTime(reply.timePosted)
                replyLikesTV.text = reply.likeCount.toString() + " likes"

                deleteIv.setOnClickListener {
                    onReplyClicked(reply)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RepliesBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding, onReplyClicked)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }
}