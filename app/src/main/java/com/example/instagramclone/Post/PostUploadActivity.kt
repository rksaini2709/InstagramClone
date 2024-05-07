package com.example.instagramclone.Post

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.instagramclone.HomeActivity
import com.example.instagramclone.Models.UploadPost
import com.example.instagramclone.Models.User
import com.example.instagramclone.databinding.ActivityPostUploadBinding
import com.example.instagramclone.utils.POST
import com.example.instagramclone.utils.UPLOAD_POST_FOLDER
import com.example.instagramclone.utils.USER_NODE
import com.example.instagramclone.utils.uploadImage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class PostUploadActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityPostUploadBinding.inflate(layoutInflater)
    }

    var imageUrl: String? = null

    // Activity Result Launcher for picking images
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            // Handle the selected image URI here
            uploadImage(uri, UPLOAD_POST_FOLDER) { url ->
                if (url != null) {
                    binding.selectImage.setImageURI(uri)
                    imageUrl = url
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Make the status bar transparent and adjust the status bar icon colors
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        }

        // back Arrow button
        setSupportActionBar(binding.materialToolbar)
        /*supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }*/
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        binding.materialToolbar.setNavigationOnClickListener {
            startActivity(Intent(this@PostUploadActivity, HomeActivity::class.java))
            finish()
        }

        binding.selectImage.setOnClickListener {
            launcher.launch("image/*")
        }

        binding.cancelButton.setOnClickListener {
            startActivity(Intent(this@PostUploadActivity, HomeActivity::class.java))
            finish()
        }

        binding.postButton.setOnClickListener {
            Firebase.firestore.collection(USER_NODE).document().get()
                .addOnSuccessListener {
                    var user: User = it.toObject<User>()!!

                    val post: UploadPost = UploadPost(
                        imageUrl!!,
                        caption = binding.caption.editableText?.toString() ?: "",
                        profileLink = user?.image ?: "",
                        uid = Firebase.auth.currentUser!!.uid,
                        time = System.currentTimeMillis().toString() ?: ""
                    )

                    Firebase.firestore.collection(POST).document().set(post).addOnSuccessListener {
                        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid).document()
                            .set(post).addOnSuccessListener {
                            startActivity(Intent(this@PostUploadActivity, HomeActivity::class.java))
                            finish()
                        }
                    }
                }
        }
    }
}