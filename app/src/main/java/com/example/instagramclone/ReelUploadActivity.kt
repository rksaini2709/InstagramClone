package com.example.instagramclone

// Import necessary dependencies and modules
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.instagramclone.Models.UploadReel
import com.example.instagramclone.Models.User
import com.example.instagramclone.databinding.ActivityReelUploadBinding
import com.example.instagramclone.utils.REEL
import com.example.instagramclone.utils.UPLOAD_REEL_FOLDER
import com.example.instagramclone.utils.USER_NODE
import com.example.instagramclone.utils.uploadReel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

// ReelUploadActivity class definition
class ReelUploadActivity : AppCompatActivity() {

    // Lazily initialize the binding for the activity
    private val binding by lazy {
        ActivityReelUploadBinding.inflate(layoutInflater)
    }

    // Variable to hold the URL of the uploaded reel
    private var reelUrl: String? = null // Initialize with null

    // Progress dialog for showing upload progress
    private lateinit var progressDialog: ProgressDialog

    // Activity result launcher for picking a reel from the gallery
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            // Upload the selected reel to the upload reel folder
            uploadReel(uri, UPLOAD_REEL_FOLDER, progressDialog) { url ->
                // Handle the result of reel upload
                if (url != null) {
                    // Store the URL of the uploaded reel
                    reelUrl = url

                    // Log the updated reelUrl
                    Log.d("ReelUploadActivity", "Reel URL after upload: $reelUrl")
                }
            }
        }
    }

    // onCreate function
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the content view to the root view of the binding
        setContentView(binding.root)

        // Make the status bar transparent and adjust the status bar icon colors
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        }

        // Initialize the progressDialog variable
        progressDialog = ProgressDialog(this)

        // Set the custom toolbar as the support action bar
        setSupportActionBar(binding.materialToolbar)

        // Enable the Up button for navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Set click listener for selecting a reel
        binding.selectReel.setOnClickListener {
            // Launch the reel picker activity
            launcher.launch("video/*")
        }

        // Log the value of reelUrl when selecting a reel
        Log.d("ReelUploadActivity", "Reel URL on selection: $reelUrl")

        // Set click listener for the cancel button
        binding.cancelButton.setOnClickListener {
            // Start the HomeActivity if the user cancels uploading reel
            startActivity(Intent(this@ReelUploadActivity, HomeActivity::class.java))
            // Finish the activity
            finish()
        }

        // Set click listener for the post button
        binding.postButton.setOnClickListener {

            // Access Firebase Firestore to fetch user information
            Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {
                // Retrieve user information and convert it into a User object
                var user : User = it.toObject<User>()!!

                // Ensure that a reel is selected
                if (reelUrl != null) {
                    // Create an UploadReel object with the reel URL, caption, and user profile image URL
                    val reel = UploadReel(reelUrl!!, binding.caption.editableText?.toString() ?: "" ,user.image!!)

                    // Save the reel to Firestore
                    val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
                    currentUserUid?.let { uid ->
                        val firestore = FirebaseFirestore.getInstance()
                        val reelRef = firestore.collection(REEL).document()
                        reelRef.set(reel)
                            .addOnSuccessListener {
                                // Save the reel to the user's collection in Firestore
                                firestore.collection(uid + REEL).document(reelRef.id)
                                    .set(reel)
                                    .addOnSuccessListener {
                                        // Start the HomeActivity after successful upload
                                        startActivity(Intent(this@ReelUploadActivity, HomeActivity::class.java))
                                        // Finish the activity
                                        finish()
                                    }
                                    .addOnFailureListener { e ->
                                        // Handle failure to upload reel to user's collection
                                        Toast.makeText(
                                            this@ReelUploadActivity,
                                            "Failed to upload reel: ${e.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }
                            .addOnFailureListener { e ->
                                // Handle failure to upload reel to global collection
                                Toast.makeText(
                                    this@ReelUploadActivity,
                                    "Failed to upload reel: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
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
}