package com.example.instagramclone.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.instagramclone.Models.User
import com.example.instagramclone.SignUpActivity
import com.example.instagramclone.adapters.ViewPagerAdapter
import com.example.instagramclone.databinding.FragmentProfileBinding
import com.example.instagramclone.utils.USER_NODE
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout using data binding
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Set click listener for the edit profile button
        binding.editProfile.setOnClickListener {
            // Create intent to launch SignUpActivity for profile update
            val intent = Intent(activity, SignUpActivity::class.java)
            intent.putExtra("MODE", 1)
            startActivity(intent)
            activity?.finish() // Finish current activity to prevent going back to it when SignUpActivity finishes
        }

        // Initialize and set up the ViewPager adapter with fragments
        viewPagerAdapter = ViewPagerAdapter(requireActivity())
        viewPagerAdapter.addFragment(MyPostFragment(), "My Post")
        viewPagerAdapter.addFragment(MyReelFragment(), "My Reel")
        viewPagerAdapter.addFragment(TagFragment(), "Tags")
        binding.viewPager.adapter = viewPagerAdapter

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        // Fetch user data from Firestore and update UI
        Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get()
            .addOnSuccessListener { document ->
                val user = document.toObject<User>()
                if (user != null) {
                    binding.name.text = user.name
                    binding.bio.text = user.email
                    // Load user's profile image if it's not null or empty
                    if (!user.image.isNullOrEmpty()) {
                        Picasso.get().load(user.image).into(binding.profileImage)
                    }
                }
            }
    }
}