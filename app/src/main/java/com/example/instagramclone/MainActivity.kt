package com.example.instagramclone

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Color
import android.os.Build
import android.view.View
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the layout resource for the activity
        setContentView(R.layout.activity_main)

        // Make the status bar transparent and adjust the status bar icon colors
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        }

        // Use Handler to delay the execution of code for 3 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            // Check if a user is authenticated using Firebase Authentication
            if (FirebaseAuth.getInstance().currentUser == null) {
                // If no user is authenticated, navigate to the SignUpActivity
                startActivity(Intent(this, SignUpActivity::class.java))
            } else {
                // If a user is authenticated, navigate to the HomeActivity
                startActivity(Intent(this, HomeActivity::class.java))
            }
            // Finish the current activity to prevent going back to it
            finish()
        }, 3000) // Delay for 3 seconds
    }
}
