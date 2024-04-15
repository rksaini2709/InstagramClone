package com.example.instagramclone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagramclone.Models.UploadPost
import com.example.instagramclone.Models.User
import com.example.instagramclone.R
import com.example.instagramclone.databinding.LatestUploadedPostBinding
import com.example.instagramclone.utils.USER_NODE
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class PostAdapter(
    private val context: Context,
    private val postList: ArrayList<UploadPost>
) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    // Inner ViewHolder class to hold the views for each item in the RecyclerView
    inner class ViewHolder(val binding: LatestUploadedPostBinding) : RecyclerView.ViewHolder(binding.root)

    // Creating ViewHolder instances
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflating the layout for each item in the RecyclerView
        val inflater = LayoutInflater.from(parent.context)
        val binding = LatestUploadedPostBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    // Getting the total number of items in the RecyclerView
    override fun getItemCount(): Int {
        return postList.size
    }

    // Binding data to the views in each item of the RecyclerView
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = postList[position]

        try{
            // Fetching user data from Firestore using UID of the user associated with the post
            Firebase.firestore.collection(USER_NODE).document(post.uid)
                .get().addOnSuccessListener { documentSnapshot ->
                    // Converting the retrieved Firestore document to a User object
                    val user = documentSnapshot.toObject<User>()

                    // Loading user profile image using Glide library
                    Glide.with(context)
                        .load(user?.image)
                        .placeholder(R.drawable.profile_icon) // Placeholder image while loading
                        .into(holder.binding.profileImage) // Setting the loaded image to the ImageView

                    // Setting the name of the user to the TextView in the layout
                    holder.binding.name.text = user?.name
                }
        }catch(e: Exception){
            // Handle exceptions if any
        }

        // Loading post image using Glide library
        Glide.with(context)
            .load(post.uploadPostUrl)
            .placeholder(R.drawable.loading) // Placeholder image while loading
            .into(holder.binding.postedImage) // Setting the loaded image to the ImageView

        // Setting the time and caption of the post to the TextViews in the layout
        holder.binding.postTime.text = post.time
        holder.binding.caption.text = post.caption
    }
}
