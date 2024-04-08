package com.example.instagramclone.utils

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

// Function to upload an image to Firebase Storage and return its URL
fun uploadImage(uri: Uri, folderName: String, callback: (String?) -> Unit) {

    // Variable to store the image URL, initially null
    var imageUrl: String? = null

    // Get a reference to the Firebase Storage instance and specify the folder to upload the image
    FirebaseStorage.getInstance().getReference(folderName)
        .child(
            UUID.randomUUID().toString()
        ) // Generate a random UUID for the image file name to ensure uniqueness
        .putFile(uri)  // Upload the image file to Firebase Storage
        .addOnSuccessListener {  // Add a listener to handle the success event after uploading the image

            // Once the image is uploaded successfully, get its download URL
            // Add a listener to handle the success event after getting the download URL
            it.storage.downloadUrl
                .addOnSuccessListener { downloadUri ->
                    // Assign the download URL to the imageUrl variable
                    imageUrl = downloadUri.toString()

                    // Invoke the callback function with the imageUrl as its argument
                    callback(imageUrl)
                }
        }
}

// Function to upload a reel to Firebase Storage and return its URL
fun uploadReel(
    uri: Uri,
    folderName: String,
    progressDialog: ProgressDialog,
    callback: (String?) -> Unit
) {

    // Variable to store the reel URL, initially null
    var reelUrl: String? = null

    // Set title for the progress dialog
    progressDialog.setTitle("Uploading . . . ")

    // Get a reference to the Firebase Storage instance and specify the folder to upload the reel
    FirebaseStorage.getInstance().getReference(folderName)
        .child(
            UUID.randomUUID().toString()
        ) // Generate a random UUID for the reel file name to ensure uniqueness
        .putFile(uri)  // Upload the reel file to Firebase Storage
        .addOnSuccessListener {  // Add a listener to handle the success event after uploading the reel

            // Once the reel is uploaded successfully, get its download URL
            // Add a listener to handle the success event after getting the download URL
            it.storage.downloadUrl
                .addOnSuccessListener { downloadUri ->
                    // Assign the download URL to the reelUrl variable
                    reelUrl = downloadUri.toString()

                    // Invoke the callback function with the reelUrl as its argument
                    callback(reelUrl)
                }
        }
        .addOnProgressListener {
            // Calculate the percentage of uploaded data
            val uploadedPercentage: Long = (it.bytesTransferred * 100 / it.totalByteCount)

            // Update progress message in the progress dialog
            progressDialog.setMessage("Uploaded $uploadedPercentage%")
        }
}
