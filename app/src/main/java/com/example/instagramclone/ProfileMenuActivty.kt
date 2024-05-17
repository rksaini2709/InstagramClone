package com.example.instagramclone

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.instagramclone.databinding.ActivityProfileMenuActivtyBinding
import com.example.instagramclone.fragments.ProfileFragment

class ProfileMenuActivty : AppCompatActivity() {
    val binding by lazy {
        ActivityProfileMenuActivtyBinding.inflate(layoutInflater)
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
            startActivity(Intent(this@ProfileMenuActivty, ProfileFragment::class.java))
            finish()
        }

        binding.logout.setOnClickListener {
            startActivity(Intent(this@ProfileMenuActivty, SignUpActivity::class.java))
        }
    }
}