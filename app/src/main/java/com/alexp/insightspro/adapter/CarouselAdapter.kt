package com.alexp.insightspro.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alexp.insightspro.databinding.CarouselItemBinding
import com.bumptech.glide.Glide

class CarouselAdapter(private var imageList: List<String?>): RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder>() {

    class CarouselViewHolder(val binding: CarouselItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val binding = CarouselItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarouselViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        val image = imageList[position]
        holder.binding.apply {
            Glide.with(postImage).load(image).into(postImage)
        }
    }

    override fun getItemCount(): Int = imageList.size
}