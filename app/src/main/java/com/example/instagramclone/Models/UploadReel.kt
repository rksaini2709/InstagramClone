package com.example.instagramclone.Models

class UploadReel {

    // Mutable property to store the URL of the uploaded reel
    var uploadReelUrl: String = ""

    // Mutable property to store the caption associated with the uploaded post
    var caption: String = ""

    // Default constructor with no parameters
    constructor()

    // Secondary constructor with parameters to initialize uploadReelUrl and caption
    constructor(uploadReelUrl: String, caption: String) {
        this.uploadReelUrl = uploadReelUrl
        this.caption = caption
    }
}