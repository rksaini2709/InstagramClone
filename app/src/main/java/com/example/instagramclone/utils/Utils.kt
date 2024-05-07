package com.example.instagramclone.utils

import android.app.ProgressDialog
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

// Function to upload an image to Firebase Storage and return its URL
fun uploadImage(uri: Uri, folderName: String, callback: (String?) -> Unit) {

    // Variable to store the image URL, initially null
    var imageUrl: String? = null

    // Generate a random UUID for the image file name to ensure uniqueness
    // Upload the image file to Firebase Storage
    // Add a listener to handle the success event after uploading the image
    FirebaseStorage.getInstance().getReference(folderName).child(UUID.randomUUID().toString())
        .putFile(uri).addOnSuccessListener {

        // Once the image is uploaded successfully, get its download URL
        // Add a listener to handle the success event after getting the download URL
        it.storage.downloadUrl.addOnSuccessListener {

            // Assign the download URL to the imageUrl variable
            imageUrl = it.toString()
            callback(imageUrl)
        }
    }
}

// Function to upload a reel to Firebase Storage
fun uploadReel(
    uri: Uri,
    folderName: String,
    progressDialog: ProgressDialog,
    callback: (String?) -> Unit
) {

    // Variable to store the reel URL, initially null
    var videoUrl: String? = null

    // Set the title for the progress dialog to indicate that the upload process is ongoing
    progressDialog.setTitle("Uploading . . . ")

    // Display the progress dialog on the screen to indicate that the upload process is starting
    progressDialog.show()

    // Generate a random UUID for the image file name to ensure uniqueness
    // Upload the reel file to Firebase Storage
    // Add a listener to handle the success event after uploading the reel
    FirebaseStorage.getInstance().getReference(folderName).child(UUID.randomUUID().toString())
        .putFile(uri).addOnSuccessListener {

        // Once the reel is uploaded successfully, get its download URL
        // Add a listener to handle the success event after getting the download URL
        it.storage.downloadUrl.addOnSuccessListener {

            // Assign the download URL to the reelUrl variable
            videoUrl = it.toString()
            progressDialog.dismiss() // Dismiss the progress dialog
            callback(videoUrl) // Invoke the callback function with the reelUrl as its argument
        }
    }
        .addOnProgressListener {
            // Calculate the percentage of uploaded data
            var uploadedPercentage: Long = (it.bytesTransferred / it.totalByteCount) * 100
            // Update progress message in the progress dialog
            progressDialog.setMessage("Uploaded $uploadedPercentage%")
        }
}