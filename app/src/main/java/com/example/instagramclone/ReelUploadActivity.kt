package com.example.instagramclone

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.instagramclone.Models.UploadReel
import com.example.instagramclone.Models.User
import com.example.instagramclone.databinding.ActivityReelUploadBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage

class ReelUploadActivity : AppCompatActivity() {

    // Lazily initialize the binding variable
    private val binding by lazy { ActivityReelUploadBinding.inflate(layoutInflater) }

    // Initialize variables
    private var reelUrl: String? = null
    private lateinit var progressDialog: ProgressDialog

    // ActivityResultLauncher for selecting a reel
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            // If the result is OK, get the selected image URI and upload the reel to Firebase
            val selectedImageUri = result.data?.data
            selectedImageUri?.let {
                uploadReelToFirebase(it)
            }
        } else {
            // If the result is not OK, show an error message
            Log.e("ReelUploadActivity", "Error picking reel: Result code ${result.resultCode}")
            Toast.makeText(this, "Error picking reel", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Initialize progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        progressDialog.max = 100
        progressDialog.setMessage("Uploading Reel...")

        // Set up toolbar
        setSupportActionBar(binding.materialToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Set click listeners for buttons
        binding.selectReel.setOnClickListener {
            requestStoragePermission()
        }

        // Set click listener for the cancel button
        binding.cancelButton.setOnClickListener {
            startActivity(Intent(this@ReelUploadActivity, HomeActivity::class.java))
            finish()
        }

        // Set click listener for the post button
        binding.postButton.setOnClickListener {
            FirebaseAuth.getInstance().currentUser?.uid?.let { uid ->

                // Access Firebase Firestore to fetch user information
                FirebaseFirestore.getInstance().collection(USER_NODE).document(uid).get().addOnSuccessListener { document ->

                    // Ensure that a reel is selected
                    val user = document.toObject<User>()
                    if (reelUrl != null) {

                        // Create an UploadReel object with the reel URL, caption, and user profile image URL
                        val reel = UploadReel(reelUrl!!, binding.caption.editableText?.toString() ?: "", user?.image ?: "")

                        val firestore = FirebaseFirestore.getInstance()
                        val reelRef = firestore.collection(REEL).document()
                        reelRef.set(reel).addOnSuccessListener {

                            // Save the reel to the user's collection in Firestore
                            firestore.collection(uid + REEL).document(reelRef.id)
                                .set(reel)
                                .addOnSuccessListener {
                                    progressDialog.dismiss()
                                    startActivity(Intent(this@ReelUploadActivity, HomeActivity::class.java))
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    progressDialog.dismiss()
                                    Toast.makeText(
                                        this@ReelUploadActivity,
                                        "Failed to upload reel: ${e.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }.addOnFailureListener { e ->
                            progressDialog.dismiss()
                            Toast.makeText(
                                this@ReelUploadActivity,
                                "Failed to upload reel: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        progressDialog.show() // Show progress dialog before starting the upload
                    } else {
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

    // Function to request storage permission
    private fun requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_CODE
            )
        } else {
            launchReelPicker()
        }
    }

    // Function to launch the reel picker activity
    private fun launchReelPicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        launcher.launch(intent)
    }

    // Handle permission request results
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchReelPicker()
            } else {
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to upload the selected reel to Firebase Storage
    private fun uploadReelToFirebase(uri: Uri) {
        val uploader = FirebaseStorage.getInstance().reference.child("Reel/${System.currentTimeMillis()}")
        progressDialog.show() // Show progress dialog before starting the upload
        uploader.putFile(uri)
            .addOnProgressListener { snapshot ->
                val progress = (100.0 * snapshot.bytesTransferred / snapshot.totalByteCount).toInt()
                progressDialog.progress = progress
                progressDialog.setMessage("Uploading Reel... $progress%")
            }
            .addOnCompleteListener { task ->
                progressDialog.dismiss() // Dismiss progress dialog after upload is complete
                if (task.isSuccessful) {
                    uploader.downloadUrl.addOnSuccessListener { url ->
                        reelUrl = url.toString()
                        Log.d("ReelUploadActivity", "Reel uploaded successfully. URL: $reelUrl")
                    }
                } else {
                    Log.e("ReelUploadActivity", "Error uploading reel: ${task.exception}")
                    Toast.makeText(this@ReelUploadActivity, "Failed to upload reel", Toast.LENGTH_SHORT).show()
                }
            }
    }

    companion object {
        private const val STORAGE_PERMISSION_CODE = 100
        private const val USER_NODE = "User"
        private const val REEL = "Reels"
    }
}