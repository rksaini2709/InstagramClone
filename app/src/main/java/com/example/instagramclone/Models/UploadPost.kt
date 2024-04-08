package com.example.instagramclone.Models

// This class represents an uploaded post in the Instagram clone application
class UploadPost {

    // Mutable property to store the URL of the uploaded post
    var uploadPostUrl: String = ""

    // Mutable property to store the caption associated with the uploaded post
    var caption: String = ""

    // Default constructor with no parameters
    constructor()

    // Secondary constructor with parameters to initialize uploadPostUrl and caption
    constructor(uploadPostUrl: String, caption: String) {
        this.uploadPostUrl = uploadPostUrl
        this.caption = caption
    }
}