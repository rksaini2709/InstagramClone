package com.example.instagramclone

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.instagramclone.Models.UploadPost
import com.example.instagramclone.databinding.ActivityPostUploadBinding
import com.example.instagramclone.utils.POST
import com.example.instagramclone.utils.UPLOAD_POST_FOLDER
import com.example.instagramclone.utils.uploadImage
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class PostUploadActivity : AppCompatActivity() {

    // Lazily initialize the binding for the activity
    val binding by lazy {
        ActivityPostUploadBinding.inflate(layoutInflater)
    }

    // Variable to hold the URL of the uploaded image
    var imageUrl: String? = null

    // Activity result launcher for picking an image from the gallery
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            // Upload the selected image to the upload post folder
            uploadImage(uri, UPLOAD_POST_FOLDER) { url ->
                // Handle the result of image upload
                if (url != null) {
                    // Set the selected image URI to the ImageView in the layout
                    binding.selectImage.setImageURI(uri)

                    // Store the URL of the uploaded image
                    imageUrl = url
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge display for the activity
        enableEdgeToEdge()

        // Set the content view to the root view of the binding
        setContentView(binding.root)


        // Set the custom toolbar as the support action bar
        setSupportActionBar(binding.materialToolbar)

        // Enable the Up button for navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Set the custom toolbar as the support action bar
        setSupportActionBar(binding.materialToolbar)

        // Enable the Up button for navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Set click listener for the toolbar navigation icon
        binding.materialToolbar.setNavigationOnClickListener {
            finish()
        }

        // Set click listener for selecting an image
        binding.selectImage.setOnClickListener {
            // Launch the image picker activity
            launcher.launch("image/*")
        }

        // Set click listener for the cancel button
        binding.cancelButton.setOnClickListener {
            // Start the HomeActivity if we don't want to upload image and after tap to cancel button
            startActivity(Intent(this@PostUploadActivity, HomeActivity::class.java))
            // Finish the activity
            finish()
        }

        // Set click listener for the post button
        binding.postButton.setOnClickListener {
            // Ensure that an image is selected
            if (imageUrl != null) {
                // Get the caption entered by the user
                val caption = binding.caption.text.toString()

                // Create a Post object with the image URL and caption
                val post = UploadPost(imageUrl!!, caption)

                // Save the post to Firestore
                Firebase.firestore.collection(POST).document().set(post).addOnSuccessListener {

                    // Save the post to the user's collection in Firestore
                    Firebase.firestore.collection(Firebase.auth.currentUser!!.uid).document()
                        .set(post).addOnSuccessListener {
                        // Start the HomeActivity after successful upload
                        startActivity(Intent(this@PostUploadActivity, HomeActivity::class.java))
                        // Finish the activity
                        finish()
                    }.addOnFailureListener {
                        // Handle failure to upload post to user's collection
                        Toast.makeText(
                            this@PostUploadActivity,
                            "Failed to upload post",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }.addOnFailureListener {
                    // Handle failure to upload post to global collection
                    Toast.makeText(
                        this@PostUploadActivity,
                        "Failed to upload post",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                // Display a message indicating that no image is selected
                Toast.makeText(
                    this@PostUploadActivity,
                    "Please select an image first",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}