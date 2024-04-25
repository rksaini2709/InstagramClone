package com.example.instagramclone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagramclone.Models.UploadPost
import com.example.instagramclone.Models.User
import com.example.instagramclone.R
import com.example.instagramclone.databinding.HomePostDesignBinding
import com.example.instagramclone.utils.USER_NODE
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class PostAdapter(
    private val context: Context,
    private val postList: ArrayList<UploadPost>
) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    // Inner ViewHolder class to hold the views for each item in the RecyclerView
    inner class ViewHolder(val binding: HomePostDesignBinding) : RecyclerView.ViewHolder(binding.root)

    // Creating ViewHolder instances
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflating the layout for each item in the RecyclerView
        val inflater = LayoutInflater.from(parent.context)
        val binding = HomePostDesignBinding.inflate(inflater, parent, false)
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
                        .placeholder(R.drawable.user_profile_image) // Placeholder image while loading
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
            .into(holder.binding.postImage) // Setting the loaded image to the ImageView

        // Setting the time and caption of the post to the TextViews in the layout
        // holder.binding.postTime.text = TimeAgo.using(post.time.toLong()) // Using the TimeAgo class
        // holder.binding.postTime.text = post.time
        holder.binding.caption.text = post.caption

        // Flag to keep track of like/unlike state
        var isLiked = false

        // OnClickListener for post like ImageView
        holder.binding.postLike.setOnClickListener {
            // Toggle the like/unlike state
            isLiked = !isLiked

            // Change the resource of postLike ImageView based on like/unlike state
            val drawableResId = if (isLiked) {
                // If liked, set the liked icon
                R.drawable.heart_after_post_like
            } else {
                // If not liked, set the regular icon
                R.drawable.heart_icon
            }
            holder.binding.postLike.setImageResource(drawableResId)
        }
    }
}