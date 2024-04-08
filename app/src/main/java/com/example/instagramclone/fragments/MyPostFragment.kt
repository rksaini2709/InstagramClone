package com.example.instagramclone.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.instagramclone.Models.UploadPost
import com.example.instagramclone.adapters.UploadPostOnProfileAdapter
import com.example.instagramclone.databinding.FragmentMyPostBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class MyPostFragment : Fragment() {
    private lateinit var binding : FragmentMyPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This method is called when the fragment is created.
        // Any initialization logic specific to the fragment can be performed here.
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // This method is responsible for inflating the layout for the fragment.
        // It returns the root view of the inflated layout.
        // The 'binding' variable is initialized with the inflated binding object.
        binding = FragmentMyPostBinding.inflate(inflater, container, false)

        // Initialize an empty list to hold UploadPost objects
        val postList = ArrayList<UploadPost>()

        // Initialize the adapter with the context and the empty post list
        val adapter = UploadPostOnProfileAdapter(requireContext(), postList)

        // Set up the RecyclerView with a StaggeredGridLayoutManager
        binding.uploadedPostOnProfile.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)

        // Set the adapter for the RecyclerView
        binding.uploadedPostOnProfile.adapter = adapter

        // Fetch posts from Firestore
        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener { querySnapshot ->
            val tempList = arrayListOf<UploadPost>()
            for (document in querySnapshot.documents) {
                // Convert each document to an UploadPost object and add it to the temporary list
                val post: UploadPost = document.toObject<UploadPost>()!!
                tempList.add(post)
            }
            // Add all the fetched posts to the main post list
            postList.addAll(tempList)
            // Notify the adapter that the data set has changed
            adapter.notifyDataSetChanged()
        }

        return binding.root
    }

    companion object {
        // This companion object can be used to define any static methods or variables.
        // It's commonly used for defining static factory methods to create instances of the fragment.
        // However, in this case, it's empty.
    }
}