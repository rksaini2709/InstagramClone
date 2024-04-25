package com.example.instagramclone.Post

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
import com.example.instagramclone.HomeActivity
import com.example.instagramclone.Models.UploadPost
import com.example.instagramclone.databinding.ActivityPostUploadBinding
import com.example.instagramclone.utils.POST
import com.example.instagramclone.utils.UPLOAD_POST_FOLDER
import com.example.instagramclone.utils.uploadImage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PostUploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostUploadBinding
    private var imageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        }

        setSupportActionBar(binding.materialToolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        binding.materialToolbar.setNavigationOnClickListener {
            finish()
        }

        binding.selectImage.setOnClickListener {
            if (isStoragePermissionGranted()) {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
            } else {
                requestStoragePermission()
            }
        }

        binding.cancelButton.setOnClickListener {
            startActivity(Intent(this@PostUploadActivity, HomeActivity::class.java))
            finish()
        }

        binding.postButton.setOnClickListener {
            val currentUser = Firebase.auth.currentUser
            currentUser?.let { user ->
                if (imageUrl != null) {
                    val caption = binding.caption.editableText?.toString() ?: ""
                    val post = UploadPost(
                        uploadPostUrl = imageUrl!!,
                        caption = binding.caption.editableText?.toString() ?: "",
                        uid = Firebase.auth.currentUser!!.uid,
                        time = System.currentTimeMillis().toString()
                    )
                    val firestore = Firebase.firestore
                    firestore.collection(POST).document().set(post)
                        .addOnSuccessListener {
                            firestore.collection(USER_NODE).document(user.uid).collection(POST)
                                .document().set(post)
                                .addOnSuccessListener {
                                    startActivity(Intent(this@PostUploadActivity, HomeActivity::class.java))
                                    finish()
                                }.addOnFailureListener {
                                    Toast.makeText(this@PostUploadActivity, "Failed to upload post", Toast.LENGTH_SHORT).show()
                                }
                        }.addOnFailureListener {
                            Toast.makeText(this@PostUploadActivity, "Failed to upload post", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this@PostUploadActivity, "Please select an image first", Toast.LENGTH_SHORT).show()
                }
            }
        }

        if (!isStoragePermissionGranted()) {
            requestStoragePermission()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val selectedImageUri = data.data
            selectedImageUri?.let {
                uploadImage(it, UPLOAD_POST_FOLDER) { url ->
                    if (url != null) {
                        imageUrl = url
                        binding.selectImage.setImageURI(it)
                    }
                }
            }
        }
    }

    private fun isStoragePermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            STORAGE_PERMISSION_CODE
        )
    }

    companion object {
        private const val STORAGE_PERMISSION_CODE = 100
        private const val PICK_IMAGE_REQUEST_CODE = 101
        private const val USER_NODE = "User"
    }
}
