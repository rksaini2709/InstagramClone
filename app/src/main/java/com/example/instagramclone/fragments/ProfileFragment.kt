package com.example.instagramclone.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.instagramclone.Models.User
import com.example.instagramclone.ProfileMenuActivty
import com.example.instagramclone.R
import com.example.instagramclone.SignUpActivity
import com.example.instagramclone.adapters.ViewPagerAdapter
import com.example.instagramclone.databinding.FragmentProfileBinding
import com.example.instagramclone.utils.USER_NODE
import com.example.instagramclone.utils.USER_PROFILE_FOLDER
import com.example.instagramclone.utils.uploadImage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var user: User
    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Activity Result Launcher for picking images
        val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                // Handle the selected image URI here
                uploadImage(uri, USER_PROFILE_FOLDER) { imageUrl ->
                    if (imageUrl == null) {
                        // Handle the case when image upload fails
                        Toast.makeText(requireContext(), "Failed to upload image", Toast.LENGTH_SHORT).show()
                    } else {
                        // Assign the image URL to the user object
                        user.image = imageUrl

                        // Set the image URI to the ImageView in the layout
                        // This displays the selected image to the user
                        binding.profileImage.setImageURI(uri)
                    }
                }
            }
        }

        // Set onClickListener for adding profile image
        binding.addImage.setOnClickListener {
            // Launch the image picker
            launcher.launch("image/*")
        }

        binding.editProfile.setOnClickListener {
            val intent = Intent(activity, SignUpActivity::class.java)
            intent.putExtra("MODE", 1)
            startActivity(intent)
            activity?.finish()
        }
/*
        binding.addPost.setOnClickListener {
            startActivity(Intent(activity, AddFragment::class.java))
        }
*/

        binding.menuLine.setOnClickListener{
            startActivity(Intent(activity, ProfileMenuActivty::class.java))
        }

        viewPagerAdapter = ViewPagerAdapter(requireActivity().supportFragmentManager)
        viewPagerAdapter.addFragments(MyPostFragment(), "")
        viewPagerAdapter.addFragments(MyReelFragment(), "")
        viewPagerAdapter.addFragments(TagFragment(), "")
        binding.ViewPager.adapter = viewPagerAdapter
        binding.TabLayout.setupWithViewPager(binding.ViewPager)

        binding.TabLayout.getTabAt(0)?.setIcon(R.drawable.my_post)
        binding.TabLayout.getTabAt(1)?.setIcon(R.drawable.reel_icon)
        binding.TabLayout.getTabAt(2)?.setIcon(R.drawable.tag_me)

        return binding.root
    }

    private fun uploadImageToStorage(uri: Uri) {
        if (isStoragePermissionGranted()) {
            // Upload the image to storage
            uploadImage(uri, USER_PROFILE_FOLDER) { imageUrl ->
                if (imageUrl == null) {
                    Toast.makeText(requireContext(), "Failed to upload image", Toast.LENGTH_SHORT).show()
                } else {
                    // Update user profile with the new image URL
                    updateUserProfile(imageUrl)
                }
            }
        } else {
            // Request storage permission if not granted
            requestStoragePermission()
        }
    }

    private fun updateUserProfile(imageUrl: String) {
        // Update user object with new image URL
        user.image = imageUrl
        // Display the updated user profile image
        Picasso.get().load(user.image).into(binding.profileImage)
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

    override fun onStart() {
        super.onStart()
        // Load user profile when fragment starts
        loadUserProfile()
    }

    private fun loadUserProfile() {
        // Reload user profile from Firestore
        Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { it ->
                user = it.toObject<User>()!!
                // Display the user profile
                binding.name.text = user.name
                binding.bio.text = user.email
                if (!user.image.isNullOrEmpty()) {
                    Picasso.get().load(user.image).into(binding.profileImage)
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Failed to load profile: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    companion object {
        private const val STORAGE_PERMISSION_CODE = 113
    }
}