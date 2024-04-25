package com.example.instagramclone.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instagramclone.Models.UploadPost
import com.example.instagramclone.R
import com.example.instagramclone.adapters.PostAdapter
import com.example.instagramclone.databinding.FragmentHomeBinding
import com.example.instagramclone.utils.POST
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

// HomeFragment class definition
class HomeFragment : Fragment() {

    // Binding object for the fragment layout
    private lateinit var binding: FragmentHomeBinding

    private var postList = ArrayList<UploadPost>()

    private lateinit var adapter: PostAdapter

    // onCreate method
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    // onCreateView method
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment using the binding object
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        adapter = PostAdapter(requireContext(), postList)

        binding.homeRecycleView.layoutManager = LinearLayoutManager(requireContext())

        binding.homeRecycleView.adapter = adapter

        // This fragment has an options menu, so we need to set this flag to true
        setHasOptionsMenu(true)

        // Set the toolbar as the support action bar for the activity
        // (Assuming the toolbar is named materialToolbar2 in the fragment layout)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.materialToolbar2)

        // Set the title for the toolbar
        (requireActivity() as AppCompatActivity).supportActionBar?.title = null

        // Fetching posts from Firestore
        Firebase.firestore.collection(POST).get().addOnSuccessListener {

            // Temporary list to hold fetched posts
            var tempList = ArrayList<UploadPost>()

            // Clear existing postList
            postList.clear()

            // Loop through each document in the result
            for (document in it.documents) {
                // Convert Firestore document to UploadPost object
                var post : UploadPost = document.toObject<UploadPost>()!!

                // Add post to temporary list
                tempList.add(post)
            }

            // Add all posts from temporary list to postList
            postList.addAll(tempList)

            // Notify adapter that dataset has changed
            adapter.notifyDataSetChanged()
        }

        // Return the root view of the fragment layout
        return binding.root
    }

    // onCreateOptionsMenu method to inflate the options menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Inflate the menu layout into the provided Menu object
        inflater.inflate(R.menu.notification_msg_menu, menu)
        // Call the superclass implementation of this method
        super.onCreateOptionsMenu(menu, inflater)
    }
}