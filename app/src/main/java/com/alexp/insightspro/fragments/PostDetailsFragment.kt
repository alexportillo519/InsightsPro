package com.alexp.insightspro.fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.alexp.insightspro.MainViewModel
import com.alexp.insightspro.adapter.CarouselAdapter
import com.alexp.insightspro.databinding.FragmentPostDetailsBinding
import com.alexp.insightspro.models.Post
import com.alexp.insightspro.networking.Network
import com.bumptech.glide.Glide

class PostDetailsFragment : Fragment() {

    private lateinit var binding: FragmentPostDetailsBinding
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostDetailsBinding.inflate(inflater, container, false)

        setObservers()

        return binding.root
    }

    private fun setObservers() {
        mainViewModel.postClicked.observe(viewLifecycleOwner) { postClicked ->
            checkMediaType(postClicked)
        }
    }

    private fun checkMediaType(post: Post) {
        when (post.mediaType) {
            "VIDEO" -> {
                val uri: Uri = Uri.parse(post.postUrl)
                binding.videoConstraintLayout.visibility = View.VISIBLE
                binding.imageConstraintLayout.visibility = View.INVISIBLE
                binding.carouselConstraintLayout.visibility = View.INVISIBLE
                binding.postVideoView.setVideoURI(uri)
                binding.postVideoView.setMediaController(null)
                binding.postVideoView.setOnPreparedListener { it.isLooping = true }
                binding.postVideoView.start()
                binding.videoCaption.text = post.caption
                binding.videoLikeCountTV.text = post.likeCount.toString()
                binding.videoCommentCountTV.text = post.commentCount.toString()
            }
            "CAROUSEL_ALBUM" -> {
                binding.circularProgressBar.visibility = View.VISIBLE
                Network.getCarouselData(post.postId, mainViewModel.accessToken.value) { listOfImages ->
                    val carouselAdapter = CarouselAdapter(listOfImages)
                    binding.carouselRecyclerView.apply {
                        adapter = carouselAdapter
                        binding.circularProgressBar.visibility = View.INVISIBLE
                    }
                    binding.videoConstraintLayout.visibility = View.INVISIBLE
                    binding.imageConstraintLayout.visibility = View.INVISIBLE
                    binding.carouselConstraintLayout.visibility = View.VISIBLE
                    binding.carouselCaptionTV.text = post.caption
                    binding.carouselLikeCountTV.text = post.likeCount.toString()
                    binding.carouselCommentCountTV.text = post.commentCount.toString()
                }
            }
            else -> {
                binding.imageConstraintLayout.visibility = View.VISIBLE
                binding.videoConstraintLayout.visibility = View.INVISIBLE
                binding.carouselConstraintLayout.visibility = View.INVISIBLE
                binding.imageCaptionTV.text = post.caption
                binding.imageLikeCountTV.text = post.likeCount.toString()
                binding.imageCommentCountTV.text = post.commentCount.toString()
                Glide.with(binding.root).load(post.postUrl).into(binding.postImageView)
            }
        }
    }
}