package com.example.instagramclone.Post

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.instagramclone.HomeActivity
import com.example.instagramclone.Models.UploadReel
import com.example.instagramclone.Models.User
import com.example.instagramclone.databinding.ActivityReelUploadBinding
import com.example.instagramclone.utils.REEL
import com.example.instagramclone.utils.UPLOAD_REEL_FOLDER
import com.example.instagramclone.utils.USER_NODE
import com.example.instagramclone.utils.uploadReel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class ReelUploadActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityReelUploadBinding.inflate(layoutInflater)
    }

    private lateinit var videoUrl: String

    private lateinit var progressDialog: ProgressDialog


    // Activity Result Launcher for picking images
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            // Handle the selected image URI here
            uploadReel(uri, UPLOAD_REEL_FOLDER, progressDialog) { url ->
                if (url != null) {
                    videoUrl = url
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Create the ProgressDialog
        progressDialog = ProgressDialog(this)

        // Make the status bar transparent and adjust the status bar icon colors
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
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
            startActivity(Intent(this@ReelUploadActivity, HomeActivity::class.java))
            finish()
        }

        binding.selectReel.setOnClickListener {
            launcher.launch("video/*")
        }

        binding.cancelButton.setOnClickListener {
            startActivity(Intent(this@ReelUploadActivity, HomeActivity::class.java))
            finish()
        }

        binding.postButton.setOnClickListener {
            Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get()
                .addOnSuccessListener {
                    var user: User = it.toObject<User>()!!

                    /*val reel =
                        UploadReel(videoUrl!!, binding.caption.editableText?.toString() ?: "", user?.image ?: "")*/
                    val reel = UploadReel(
                        videoUrl!!,
                        binding.caption.editableText?.toString() ?: "",
                        user?.image ?: "",
                        user?.name ?: ""
                    )

                    Firebase.firestore.collection(REEL).document().set(reel).addOnSuccessListener {
                        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + REEL)
                            .document()
                            .set(reel).addOnSuccessListener {
                                startActivity(
                                    Intent(
                                        this@ReelUploadActivity,
                                        HomeActivity::class.java
                                    )
                                )
                                finish()
                            }
                    }
                }
        }
    }
}