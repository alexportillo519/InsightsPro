package com.alexp.insightspro.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alexp.insightspro.databinding.PostBinding
import com.alexp.insightspro.models.Comment
import com.alexp.insightspro.models.Post
import com.bumptech.glide.Glide

class PostAdapter(
    private val onPostClicked: (post: Post) -> Unit
) : ListAdapter<Post, PostAdapter.ItemViewHolder>(diff) {

    companion object {
        val diff = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem == newItem
            }

        }
    }

    class ItemViewHolder(
        private val binding: PostBinding,
        private val onPostClicked: (post: Post) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(post: Post) {
            binding.apply {
                Glide.with(binding.root).load(post.postUrl).into(binding.postImage)
                postCaption.text = post.caption
                likeCountTV.text = post.likeCount.toString()
                commentCountTV.text = post.commentCount.toString()

                postCardView.setOnClickListener {
                    onPostClicked(post)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding, onPostClicked)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

}