package com.alexp.insightspro.adapter

import android.annotation.SuppressLint
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alexp.insightspro.databinding.CommentBinding
import com.alexp.insightspro.models.Comment
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class CommentAdapter(
    private val onItemClicked: (comment: Comment) -> Unit
): ListAdapter<Comment, CommentAdapter.ItemViewHolder>(diff) {

    companion object {
        val diff = object : DiffUtil.ItemCallback<Comment>() {
            override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem == newItem
            }

        }
    }

    class ItemViewHolder(
        private val binding: CommentBinding,
        private val onItemClicked: (comment: Comment) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SimpleDateFormat")
        fun onBind(comment: Comment) {
            binding.apply {
                commentTextTV.text = comment.text

                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                sdf.timeZone = TimeZone.getTimeZone("GMT")
                val time = sdf.parse(comment.timePosted!!)?.time
                val now = System.currentTimeMillis()
                val timeAgo = DateUtils.getRelativeTimeSpanString(time!!, now, DateUtils.MINUTE_IN_MILLIS).toString()

                commentTimePostedTV.text = timeAgo
                commentUsernameTV.text = comment.userWhoCommented
                commentLayout.setOnClickListener {
                    onItemClicked(comment)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CommentBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding, onItemClicked)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }
}