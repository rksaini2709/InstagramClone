package com.example.instagramclone.Models

// This class represents an uploaded post in the Instagram clone application
class UploadPost {

    // Mutable property to store the URL of the uploaded post
    var uploadPostUrl: String = ""

    // Mutable property to store the caption associated with the uploaded post
    var caption: String = ""

    // Mutable property to store the name associated with the uploaded post
    var uid: String = ""

    // Mutable property to store the time associated with the uploaded post
    var time: String = ""

    // Default constructor with no parameters
    constructor()
    constructor(uploadPostUrl: String, caption: String) {
        this.uploadPostUrl = uploadPostUrl
        this.caption = caption
    }

    // Secondary constructor with parameters to initialize uploadPostUrl, caption, uid, and time
    constructor(uploadPostUrl: String, caption: String, uid: String, time: String) {
        this.uploadPostUrl = uploadPostUrl
        this.caption = caption
        this.uid = uid
        this.time = time
    }
}