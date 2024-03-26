package com.example.instagramclone

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.instagramclone.Models.User
import com.example.instagramclone.databinding.ActivitySignUpBinding
import com.example.instagramclone.utils.USER_NODE
import com.example.instagramclone.utils.USER_PROFILE_FOLDER
import com.example.instagramclone.utils.uploadImage
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.squareup.picasso.Picasso

class SignUpActivity : AppCompatActivity() {

    // Lazy initialization of view binding using ActivitySignUpBinding
    val binding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    // Declare a lateinit variable to hold User object
    lateinit var user: User

    // Activity Result Launcher for picking images
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            // Handle the selected image URI here
            uploadImage(uri, USER_PROFILE_FOLDER) { imageUrl ->
                if (imageUrl == null) {
                    // Handle the case when image upload fails
                    Toast.makeText(this@SignUpActivity, "Failed to upload image", Toast.LENGTH_SHORT).show()
                } else {
                    // Assign the image URL to the user object
                    user.image = imageUrl

                    // Set the image URI to the ImageView in the layout
                    // This displays the selected image to the user
                    binding.profileImage.setImageURI(uri)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Set text with multiple colors using HTML formatting
        val text = "<font color = #ff000000 > Already Have An Account </font> " +
                "<font color = #1E88E5 > Login? </font>"
        binding.Login.setText(Html.fromHtml(text))

        // Initialize user object
        user = User()

        // Check if the activity was launched to update profile
        if(intent.hasExtra("MODE")){
            if(intent.getIntExtra("MODE", -1) == 1){
                // Set button text to indicate profile update
                binding.signUpBtn.text = "Update Profile"

                // Access Firestore instance and fetch user data for profile update
                Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get()
                    .addOnSuccessListener { document ->
                        // Convert Firestore document to User object
                        user = document.toObject<User>()!!

                        // Load the user's profile image if it's not null or empty
                        if (!user.image.isNullOrEmpty()){
                            Picasso.get().load(user.image).into(binding.profileImage)
                        }

                        // Fill EditText fields with user data
                        binding.name.setText(user.name)
                        binding.email.setText(user.email)
                        binding.password.setText(user.password)
                    }
            }
        }

        // Handle sign up button click
        binding.signUpBtn.setOnClickListener {

            // Check if the activity was launched to update profile
            if(intent.hasExtra("MODE")){
                if (intent.getIntExtra("MODE", -1) == 1){
                    // Handle update profile functionality

                    // Store updated user data in Firestore
                    Firebase.firestore.collection(USER_NODE)
                        .document(Firebase.auth.currentUser!!.uid).set(user)
                        .addOnSuccessListener {
                            // After registration, navigate to HomeActivity
                            startActivity(Intent(this@SignUpActivity, HomeActivity::class.java))
                            finish()
                        }
                }
            }
            else{
                // Proceed with new user sign up

                // Check if any field is empty
                if (binding.name.text.toString().isEmpty() ||
                    binding.email.text.toString().isEmpty() ||
                    binding.password.text.toString().isEmpty()
                ) {
                    // Display a toast message prompting the user to fill all required information
                    Toast.makeText(this@SignUpActivity,
                        "Please fill all required information",
                        Toast.LENGTH_SHORT).show()
                } else
                {
                    // Create user with email and password
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        binding.email.text.toString(),
                        binding.password.text.toString()
                    ).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // If user creation is successful, store user data in Firestore
                            user.name = binding.name.text.toString()
                            user.email = binding.email.text.toString()
                            user.password = binding.password.text.toString()

                            // Store user data in Firestore
                            Firebase.firestore.collection(USER_NODE)
                                .document(Firebase.auth.currentUser!!.uid).set(user)
                                .addOnSuccessListener {
                                    // After registration, navigate to HomeActivity
                                    startActivity(Intent(this@SignUpActivity, HomeActivity::class.java))
                                    finish()
                                }
                        } else {
                            // If user creation fails, show an error message
                            Toast.makeText(this@SignUpActivity,
                                "Sign Up Failed: ${task.exception?.message}",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        // Set onClickListener for adding profile image
        binding.addImage.setOnClickListener {
            // Launch the image picker
            launcher.launch("image/*")
        }

        // Set onClickListener for navigating to LoginActivity
        binding.Login.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
            finish()
        }
    }
}
