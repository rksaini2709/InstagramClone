package com.example.instagramclone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagramclone.Models.User
import com.example.instagramclone.R
import com.example.instagramclone.databinding.SearchListViewBinding
import com.example.instagramclone.utils.FOLLOW
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

// Adapter for displaying search results in RecyclerView
class SearchAdapter(var context: Context, var userList: ArrayList<User>) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    // Inner ViewHolder class to hold the views for each item in the RecyclerView
    inner class ViewHolder(var binding: SearchListViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    // Create new ViewHolders (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the layout for each item view using LayoutInflater
        val binding =
            SearchListViewBinding.inflate(LayoutInflater.from(context), parent, false)
        // Return a ViewHolder object with the inflated layout
        return ViewHolder(binding)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        // Return the size of the user list
        return userList.size
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var isfollow = false

        // Load the user image using Glide library and set it to the dp_image ImageView
        Glide.with(context).load(userList[position].image)
            .placeholder(R.drawable.user_profile_image).into(holder.binding.dpImage)
        // Set the user's name to the search_name TextView
        holder.binding.searchName.text = userList[position].name

        // Check if the current user is following the displayed user
        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + FOLLOW)
            .whereEqualTo("email", userList[position].email).get().addOnSuccessListener {
            if (it.documents.size == 0) {
                isfollow = false
            } else {
                holder.binding.follow.text = "Unfollow"
                isfollow = true
            }
        }

        // Set click listener for follow/unfollow button
        holder.binding.follow.setOnClickListener {
            if (isfollow) {
                // If user is already following, unfollow
                Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + FOLLOW)
                    .whereEqualTo("email", userList[position].email).get().addOnSuccessListener {
                    Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + FOLLOW)
                        .document(it.documents[0].id).delete()
                    holder.binding.follow.text = "follow"
                    isfollow = false
                }
            } else {
                // If user is not following, follow
                Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + FOLLOW).document()
                    .set(userList[position])
                holder.binding.follow.text = "Unfollow"
                isfollow = true
            }
        }
    }
}