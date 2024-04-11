package com.example.instagramclone.utils

import android.app.ProgressDialog
import android.net.Uri
import android.util.Log
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

// Function to upload a reel to Firebase Storage
fun uploadReel(
    uri: Uri, // The Uri of the selected reel
    folderName: String, // The folder in Firebase Storage to upload the reel
    progressDialog: ProgressDialog, // Progress dialog to show upload progress
    callback: (String?) -> Unit // Callback function to handle the result of the upload
) {
    // Initialize a variable to store the reel URL, initially null
    var reelUrl: String? = null

    // Set the title for the progress dialog to indicate that the upload process is ongoing
    progressDialog.setTitle("Uploading . . . ")

    // Display the progress dialog on the screen to indicate that the upload process is starting
    progressDialog.show()


    // Get a reference to the Firebase Storage instance and specify the folder to upload the reel
    FirebaseStorage.getInstance().getReference(folderName)
        .child(UUID.randomUUID().toString()) // Generate a random UUID for the reel file name
        .putFile(uri) // Upload the reel file to Firebase Storage
        .addOnSuccessListener { taskSnapshot ->
            // Once the reel is uploaded successfully, get its download URL
            taskSnapshot.storage.downloadUrl
                .addOnSuccessListener { downloadUri ->
                    // Assign the download URL to the reelUrl variable
                    reelUrl = downloadUri.toString()

                    // Log the retrieved URL for debugging
                    Log.d("UploadReel", "Reel URL: $reelUrl")

                    // Dismiss the progress dialog
                    progressDialog.dismiss()

                    // Invoke the callback function with the reelUrl as its argument
                    callback(reelUrl)
                }
        }
        .addOnProgressListener { taskSnapshot ->
            // Calculate the percentage of uploaded data
            val uploadedPercentage: Long = (taskSnapshot.bytesTransferred * 100 / taskSnapshot.totalByteCount)

            // Update progress message in the progress dialog
            progressDialog.setMessage("Uploaded $uploadedPercentage%")
        }
}