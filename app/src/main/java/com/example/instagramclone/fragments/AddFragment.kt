package com.example.instagramclone.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.instagramclone.PostUploadActivity
import com.example.instagramclone.ReelUploadActivity
import com.example.instagramclone.databinding.FragmentAddBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentAddBinding // Binding object for the fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize any data or variables here
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment using data binding
        binding = FragmentAddBinding.inflate(inflater, container, false)

        // Set click listeners for each item in the bottom sheet
        // When clicked, start the corresponding activity

        // Upload Reels
        binding.reel.setOnClickListener {
            activity?.startActivity(Intent(requireContext(), ReelUploadActivity::class.java))
            activity?.finish()
        }

        // Upload Posts
        binding.post.setOnClickListener {
            activity?.startActivity(Intent(requireContext(), PostUploadActivity::class.java))
            activity?.finish()
        }

        /*
        // Upload Story
        binding.story.setOnClickListener {
            activity?.startActivity(Intent(requireContext(), StoryUploadActivity::class.java))
            activity?.finish()
        }

        // Story Highlight
        binding.storyHighlight.setOnClickListener {
            activity?.startActivity(Intent(requireContext(), StoryHighlightActivity::class.java))
            activity?.finish()
        }

        // Go Live
        binding.live.setOnClickListener {
            activity?.startActivity(Intent(requireContext(), GoLiveActivity::class.java))
            activity?.finish()
        }
        */

        return binding.root // Return the root view of the binding
    }

    companion object {
        // Any companion object functions or properties can be defined here
    }
}