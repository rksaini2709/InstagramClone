package com.example.instagramclone.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.instagramclone.Models.User
import com.example.instagramclone.R
import com.example.instagramclone.SignUpActivity
import com.example.instagramclone.adapters.ViewPagerAdapter
import com.example.instagramclone.databinding.FragmentProfileBinding
import com.example.instagramclone.utils.USER_NODE
import com.example.instagramclone.utils.USER_PROFILE_FOLDER
import com.example.instagramclone.utils.uploadImage
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var user: User // Global variable declared here
    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout using data binding
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Activity Result Launcher for picking images
        val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data?.let { uri ->
                    selectedImageUri = uri
                    binding.profileImage.setImageURI(uri)
                    uploadImageToStorage(uri)
                }
            }
        }

        // Set click listener for adding profile image
        binding.addImage.setOnClickListener {
            // Launch the image picker
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            launcher.launch(intent)
        }

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

        // Set up TabLayout with ViewPager using TabLayoutMediator
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            // Set custom view for each tab (in this case, setting icons)
            when (position) {
                0 -> tab.setIcon(R.drawable.my_post) // Set icon for "My Post" tab
                1 -> tab.setIcon(R.drawable.reel_icon) // Set icon for "My Reel" tab
                2 -> tab.setIcon(R.drawable.tag_me) // Set icon for "Tags" tab
            }
        }.attach() // Attach TabLayoutMediator

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        // Fetch user data from Firestore and update UI
        Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get()
            .addOnSuccessListener { document ->
                user = document.toObject<User>() ?: User() // Initialize user object if null
                binding.name.text = user.name
                binding.bio.text = user.email
                // Load user's profile image if it's not null or empty
                if (!user.image.isNullOrEmpty()) {
                    Picasso.get().load(user.image).into(binding.profileImage)
                }
            }
    }

    private fun uploadImageToStorage(uri: Uri) {
        if (isStoragePermissionGranted()) {
            // Upload the image to storage
            uploadImage(uri, USER_PROFILE_FOLDER) { imageUrl ->
                if (imageUrl == null) {
                    // Handle the case when image upload fails
                    Toast.makeText(requireContext(), "Failed to upload image", Toast.LENGTH_SHORT).show()
                } else {
                    // Assign the image URL to the user object
                    user.image = imageUrl
                }
            }
        } else {
            // Request storage permission if not granted
            requestStoragePermission()
        }
    }

    private fun isStoragePermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestStoragePermission() {
        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
    }

    companion object {
        private const val STORAGE_PERMISSION_CODE = 100
    }
}