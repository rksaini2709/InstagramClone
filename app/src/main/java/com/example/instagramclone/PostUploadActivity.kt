package com.example.instagramclone

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.instagramclone.Models.UploadPost
import com.example.instagramclone.databinding.ActivityPostUploadBinding
import com.example.instagramclone.utils.POST
import com.example.instagramclone.utils.UPLOAD_POST_FOLDER
import com.example.instagramclone.utils.uploadImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PostUploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostUploadBinding // Variable for view binding
    private var imageUrl: String? = null // Variable to store the URL of the uploaded image

    override fun onCreate(savedInstanceState: Bundle?) { // Function called when the activity is created
        super.onCreate(savedInstanceState)
        binding = ActivityPostUploadBinding.inflate(layoutInflater)
        setContentView(binding.root) // Set the content view to the root of the binding

        // Make the status bar transparent and adjust the status bar icon colors
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // Check if SDK version is Lollipop or higher
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR // Adjust system UI flags
            window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent) // Set status bar color to transparent
        }

        setSupportActionBar(binding.materialToolbar) // Set the custom toolbar as the support action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Enable the up button for navigation

        binding.materialToolbar.setNavigationOnClickListener { // Set click listener for the navigation icon on the toolbar
            finish() // Finish the activity when the navigation icon is clicked
        }

        binding.selectImage.setOnClickListener { // Set click listener for selecting an image
            if (isStoragePermissionGranted()) { // Check if storage permission is granted
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI) // Create intent to pick image from gallery
                startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE) // Start activity for result to pick image
            } else {
                requestStoragePermission() // Request storage permission if not granted
            }
        }

        binding.cancelButton.setOnClickListener { // Set click listener for the cancel button
            startActivity(Intent(this@PostUploadActivity, HomeActivity::class.java)) // Start HomeActivity when cancel button is clicked
            finish() // Finish the current activity
        }

        binding.postButton.setOnClickListener { // Set click listener for the post button
            val currentUser = FirebaseAuth.getInstance().currentUser // Get current user from Firebase authentication
            currentUser?.let { user -> // Check if current user is not null
                if (imageUrl != null) { // Check if image URL is not null
                    val caption = binding.caption.editableText?.toString() ?: "" // Get caption text from EditText, default to empty string
                    val post = UploadPost( // Create UploadPost object with image URL, caption, user ID, and current timestamp
                        uploadPostUrl = imageUrl!!, // Image URL
                        caption = caption, // Caption
                        uid = user.uid, // User ID
                        time = System.currentTimeMillis().toString() // Current timestamp
                    )
                    val firestore = Firebase.firestore // Get Firestore instance
                    firestore.collection(POST).document().set(post) // Upload post to global posts collection
                        .addOnSuccessListener { // On success
                            firestore.collection(USER_NODE).document(user.uid).collection(POST).document().set(post) // Upload post to user's posts collection
                                .addOnSuccessListener { // On success
                                    startActivity(Intent(this@PostUploadActivity, HomeActivity::class.java)) // Start HomeActivity
                                    finish() // Finish the activity
                                }.addOnFailureListener { // On failure
                                    Toast.makeText(this@PostUploadActivity, "Failed to upload post", Toast.LENGTH_SHORT).show() // Show failure message
                                }
                        }.addOnFailureListener { // On failure
                            Toast.makeText(this@PostUploadActivity, "Failed to upload post", Toast.LENGTH_SHORT).show() // Show failure message
                        }
                } else {
                    Toast.makeText(this@PostUploadActivity, "Please select an image first", Toast.LENGTH_SHORT).show() // Show message to select image
                }
            }
        }

        if (!isStoragePermissionGranted()) { // Check if storage permission is not granted
            requestStoragePermission() // Request storage permission
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { // Function called when activity for result returns
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) { // Check if result is from image picker and is successful
            val selectedImageUri = data.data // Get URI of selected image
            selectedImageUri?.let { // Execute code if URI is not null
                uploadImage(it, UPLOAD_POST_FOLDER) { url -> // Upload selected image to Firebase Storage
                    if (url != null) { // Check if URL is not null
                        imageUrl = url // Store uploaded image URL
                        binding.selectImage.setImageURI(it) // Set selected image URI to ImageView
                    }
                }
            }
        }
    }

    private fun isStoragePermissionGranted(): Boolean { // Function to check if storage permission is granted
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED // Return true if storage permission is granted
    }

    private fun requestStoragePermission() { // Function to request storage permission
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            STORAGE_PERMISSION_CODE
        ) // Request storage permission
    }

    companion object { // Companion object to hold constants and static methods
        private const val STORAGE_PERMISSION_CODE = 100 // Storage permission request code
        private const val PICK_IMAGE_REQUEST_CODE = 101 // Image pick request code
        private const val USER_NODE = "User" // Node name for user data in Firestore (update with actual node name)
    }
}