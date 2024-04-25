package com.example.instagramclone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagramclone.Models.User
import com.example.instagramclone.R
import com.example.instagramclone.databinding.FollowingStoryDesignBinding

class FollowingStoryDesignAdapter(var context: Context, var  followList: ArrayList<User>) : RecyclerView.Adapter<FollowingStoryDesignAdapter.ViewHolder>(){
    inner class ViewHolder(var binding : FollowingStoryDesignBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = FollowingStoryDesignBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return followList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(followList.get(position).image).placeholder(R.drawable.user_profile_image).into(holder.binding.profileImage)
        holder.binding.storyOnHome.text = followList.get(position).name
    }
}