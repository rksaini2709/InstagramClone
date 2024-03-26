package com.example.instagramclone

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.instagramclone.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using data binding
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get a reference to the BottomNavigationView
        val navView: BottomNavigationView = binding.navView

        // Find the NavController associated with the navigation host fragment
        val navController = findNavController(R.id.nav_host_fragment_activity_home)

        // Setup the BottomNavigationView with the NavController for navigation
        navView.setupWithNavController(navController)
    }
}
