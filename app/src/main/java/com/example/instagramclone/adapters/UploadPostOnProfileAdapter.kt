package com.example.instagramclone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramclone.Models.UploadPost
import com.example.instagramclone.databinding.HomePostDesignBinding
import com.squareup.picasso.Picasso

class UploadPostOnProfileAdapter(
    var context: Context,
    var postList: ArrayList<UploadPost>
) : RecyclerView.Adapter<UploadPostOnProfileAdapter.MyHolder>() {

    // ViewHolder class to hold the views for each item in the RecyclerView
    inner class MyHolder(var binding: HomePostDesignBinding) :
        RecyclerView.ViewHolder(binding.root)

    // This method is called when RecyclerView needs a new ViewHolder to represent an item.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        // Inflating the layout for each item using ViewBinding
        val binding =
            HomePostDesignBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyHolder(binding)
    }

    // This method returns the size of the dataset, i.e., the number of items to be displayed in the RecyclerView.
    override fun getItemCount(): Int {
        return postList.size
    }

    // This method is called by RecyclerView to display the data at the specified position.
    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.binding.postImage
        // Using Picasso library to load the image from the URL into the ImageView
        Picasso.get().load(postList.get(position).uploadPostUrl).into(holder.binding.postImage)
    }
}