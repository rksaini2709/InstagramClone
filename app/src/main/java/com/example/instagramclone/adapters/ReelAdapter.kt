package com.example.instagramclone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramclone.Models.UploadReel
import com.example.instagramclone.R
import com.example.instagramclone.databinding.ReelFrgmntDesignBinding
import com.squareup.picasso.Picasso

// ReelAdapter class definition
class ReelAdapter(var context: Context, var reelList: ArrayList<UploadReel>) :
    RecyclerView.Adapter<ReelAdapter.ViewHolder>() {

    // Inner ViewHolder class to hold the view references
    inner class ViewHolder(var binding: ReelFrgmntDesignBinding) :
        RecyclerView.ViewHolder(binding.root)

    // Function to create ViewHolders
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflating the layout for each item in the RecyclerView
        val binding = ReelFrgmntDesignBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    // Function to determine the number of items in the RecyclerView
    override fun getItemCount(): Int {
        return reelList.size
    }

    // Function to bind data to each item in the RecyclerView
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Ensure that the profileLink is not null before loading the image
        if (!reelList[position].profileLink.isNullOrEmpty()) {
            // Load profile image using Picasso, with a placeholder if profileLink is null or empty
            Picasso.get().load(reelList[position].profileLink)
                .placeholder(R.drawable.user_profile_image) // Placeholder image while loading
                .error(R.drawable.user_profile_image) // Optional: Error placeholder image if loading fails
                .into(holder.binding.profileImage) // Set the loaded image into the ImageView
        } else {
            // If profileLink is empty or null, set placeholder image
            holder.binding.profileImage.setImageResource(R.drawable.user_profile_image)
        }

        // Set the caption text
        holder.binding.caption.text = reelList[position].caption

        holder.binding.user.text = reelList[position].user

        // Set the video path for the video view
        holder.binding.videoView.setVideoPath(reelList[position].uploadReelUrl)

        // Set the prepared listener for the VideoView
        holder.binding.videoView.setOnPreparedListener {
            // Hide progress bar when video is prepared
            holder.binding.progressBar.visibility = View.GONE

            // Start playing the video once it's prepared
            holder.binding.videoView.start()
        }
    }

}