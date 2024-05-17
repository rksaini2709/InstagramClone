package com.example.instagramclone

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.instagramclone.Models.User
import com.example.instagramclone.databinding.ActivityLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity() {

    // Lazily initialize the view binding using ActivityLoginBinding
    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge display mode
        enableEdgeToEdge()

        // Set the content view using the inflated root view from the binding
        setContentView(binding.root)

        // Set click listener for the login button
        binding.LoginBtn.setOnClickListener {
            // Check if email or password fields are empty
            if (binding.email.text.toString().isEmpty() ||
                binding.password.text.toString().isEmpty()
            ) {
                // Display a toast message prompting the user to fill all required information
                Toast.makeText(this@LoginActivity, "Please fill all required information",
                    Toast.LENGTH_SHORT).show()
            } else {
                // Create a User object with email and password
                val user = User(
                    binding.email.editableText.toString(),
                    binding.password.editableText.toString()
                )

                // Sign in with email and password using Firebase Authentication
                Firebase.auth.signInWithEmailAndPassword(user.email!!, user.password!!)
                    .addOnCompleteListener { it ->
                        if (it.isSuccessful) {
                            // If sign-in is successful, navigate to the HomeActivity
                            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                        } else {
                            // If sign-in fails, display the error message
                            Toast.makeText(this@LoginActivity, it.exception?.localizedMessage,
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
        binding.createNewAccount.setOnClickListener{
            startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
        }
    }
}
