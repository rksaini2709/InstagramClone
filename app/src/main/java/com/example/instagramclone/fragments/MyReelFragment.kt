// MyReelFragment.kt
// This fragment is responsible for displaying a user's reel (video posts) on their profile screen.

package com.example.instagramclone.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.instagramclone.Models.UploadReel
import com.example.instagramclone.adapters.UploadReelOnProfileAdapter
import com.example.instagramclone.databinding.FragmentMyReelBinding
import com.example.instagramclone.utils.REEL
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class MyReelFragment : Fragment() {

    private lateinit var binding : FragmentMyReelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Any initialization code can be placed here
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyReelBinding.inflate(inflater, container, false)

        // Initialize an empty list to hold UploadReel objects
        val reelList = ArrayList<UploadReel>()

        // Initialize the adapter with the context and the empty reel list
        val adapter = UploadReelOnProfileAdapter(requireContext(), reelList)

        // Set up the RecyclerView with a StaggeredGridLayoutManager
        binding.uploadedReelOnProfile.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)

        // Set the adapter for the RecyclerView
        binding.uploadedReelOnProfile.adapter = adapter

        // Fetch reels from Firestore
        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + REEL).get().addOnSuccessListener { querySnapshot ->
            val tempList = arrayListOf<UploadReel>()
            for (document in querySnapshot.documents) {
                // Convert each document to an UploadReel object and add it to the temporary list
                val reel: UploadReel = document.toObject<UploadReel>()!!
                tempList.add(reel)
            }
            // Add all the fetched reels to the main reel list
            reelList.addAll(tempList)
            // Notify the adapter that the data set has changed
            adapter.notifyDataSetChanged()
        }

        return binding.root
    }

    companion object {
        // Any static variables or methods can be placed here
    }
}