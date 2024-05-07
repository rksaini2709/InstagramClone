// UploadReelOnProfileAdapter.kt
// This adapter is responsible for binding UploadReel objects to views in a RecyclerView, specifically for a user's profile screen.

package com.example.instagramclone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.instagramclone.Models.UploadReel
import com.example.instagramclone.databinding.MyPostProfileDesignBinding

class UploadReelOnProfileAdapter(
    var context: Context,
    var reelList: ArrayList<UploadReel>
) : RecyclerView.Adapter<UploadReelOnProfileAdapter.ViewHolder>() {

    // ViewHolder class to hold the views for each item in the RecyclerView
    inner class ViewHolder(var binding: MyPostProfileDesignBinding) :
        RecyclerView.ViewHolder(binding.root)

    // This method is called when RecyclerView needs a new ViewHolder to represent an item.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflating the layout for each item using ViewBinding
        val binding =
            MyPostProfileDesignBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    // This method returns the size of the dataset, i.e., the number of items to be displayed in the RecyclerView.
    override fun getItemCount(): Int {
        return reelList.size
    }

    // This method is called by RecyclerView to display the data at the specified position.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(reelList.get(position).uploadReelUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.binding.postImage)
    }
}