// Import necessary packages and libraries
package com.example.instagramclone.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.instagramclone.Models.UploadReel
import com.example.instagramclone.adapters.ReelAdapter
import com.example.instagramclone.databinding.FragmentReelBinding
import com.example.instagramclone.utils.REEL
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

// ReelFragment class extending Fragment
class ReelFragment : Fragment() {

    private lateinit var binding: FragmentReelBinding // Binding for the fragment
    lateinit var adapter: ReelAdapter // Adapter for RecyclerView
    var reelList = ArrayList<UploadReel>() // List to hold data for RecyclerView

    // Function called when the fragment is created
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = FragmentReelBinding.inflate(inflater, container, false)

        // Initialize the adapter with the context and data list
        adapter = ReelAdapter(requireContext(), reelList)

        // Set the adapter for the RecyclerView
        binding.viewPager.adapter = adapter

        // Fetch data from Firebase Firestore
        Firebase.firestore.collection(REEL).get().addOnSuccessListener { querySnapshot ->
            val tempList = ArrayList<UploadReel>() // Temporary list to hold fetched data

            // Loop through the documents fetched from Firestore
            for (document in querySnapshot.documents) {
                // Convert each document to an UploadReel object and add to the temporary list
                val reel = document.toObject<UploadReel>()
                reel?.let {
                    tempList.add(it)
                }
            }
            // Add all items from the temporary list to the data list
            reelList.addAll(tempList)
            // Notify the adapter that the data set has changed
            adapter.notifyDataSetChanged()
        }

        // Return the root view of the fragment
        return binding.root
    }
}