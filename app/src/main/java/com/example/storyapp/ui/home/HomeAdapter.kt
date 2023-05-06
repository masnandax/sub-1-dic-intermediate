package com.example.storyapp.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.storyapp.R
import com.example.storyapp.api.ListStoryItem

class HomeAdapter(private val listStory: List<ListStoryItem>): RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int)=
        ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_story, viewGroup, false))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val (idStory, imagesStory, dateStory, nameUser) = listStory[position]
        Glide.with(viewHolder.itemView.context)
            .load(imagesStory)
            .apply(RequestOptions().override(1000,1000))
            .into(viewHolder.tvImages)

        viewHolder.date.text = dateStory
        viewHolder.user.text = nameUser

        viewHolder.itemView.setOnClickListener{
            onItemClickCallback.onItemClicked(listStory[viewHolder.adapterPosition])
        }
    }

    override fun getItemCount() = listStory.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)  {
        val tvImages: ImageView = view.findViewById(R.id.images_story)
        val date: TextView = view.findViewById(R.id.date_story)
        val user: TextView = view.findViewById(R.id.user_story)
    }

    interface OnItemClickCallback {
        fun onItemClicked(story: ListStoryItem)
    }
}