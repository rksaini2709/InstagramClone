package com.example.instagramclone

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.instagramclone.Models.UploadReel
import com.example.instagramclone.databinding.ActivityReelUploadBinding
import com.example.instagramclone.utils.REEL
import com.example.instagramclone.utils.UPLOAD_REEL_FOLDER
import com.example.instagramclone.utils.uploadReel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class ReelUploadActivity : AppCompatActivity() {

    // Lazily initialize the binding for the activity
    val binding by lazy {
        ActivityReelUploadBinding.inflate(layoutInflater)
    }

    // Variable to hold the URL of the uploaded reel
    var reelUrl: String? = null

    // Progress dialog for showing upload progress
    lateinit var progressDialog: ProgressDialog

    // Activity result launcher for picking a reel from the gallery
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            // Upload the selected reel to the upload reel folder
            uploadReel(uri, UPLOAD_REEL_FOLDER, progressDialog) { url ->
                // Handle the result of reel upload
                if (url != null) {
                    // Store the URL of the uploaded reel
                    reelUrl = url
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

        // Initialize the progressDialog variable
        progressDialog = ProgressDialog(this)

        // Set message for the progress dialog
        progressDialog.setMessage("Uploading...")

        // Set click listener for selecting a reel
        binding.selectReel.setOnClickListener {
            // Launch the reel picker activity
            launcher.launch("reel/*")
        }

        // Set click listener for the cancel button
        binding.cancelButton.setOnClickListener {
            // Start the HomeActivity if the user cancels uploading reel
            startActivity(Intent(this@ReelUploadActivity, HomeActivity::class.java))
            // Finish the activity
            finish()
        }

        // Set click listener for the post button
        binding.postButton.setOnClickListener {
            // Ensure that a reel is selected
            if (reelUrl != null) {
                // Get the caption entered by the user
                val caption = binding.caption.text.toString()

                // Create an UploadReel object with the reel URL and caption
                val reel = UploadReel(reelUrl!!, caption)

                // Save the reel to Firestore
                Firebase.firestore.collection(REEL).document().set(reel).addOnSuccessListener {

                    // Save the reel to the user's collection in Firestore
                    Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + REEL).document()
                        .set(reel).addOnSuccessListener {
                            // Start the HomeActivity after successful upload
                            startActivity(Intent(this@ReelUploadActivity, HomeActivity::class.java))
                            // Finish the activity
                            finish()
                        }.addOnFailureListener {
                            // Handle failure to upload reel to user's collection
                            Toast.makeText(
                                this@ReelUploadActivity,
                                "Failed to upload reel",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }.addOnFailureListener {
                    // Handle failure to upload reel to global collection
                    Toast.makeText(
                        this@ReelUploadActivity,
                        "Failed to upload reel",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                // Display a message indicating that no reel is selected
                Toast.makeText(
                    this@ReelUploadActivity,
                    "Please select a reel first",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}