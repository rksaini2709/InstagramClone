// Model class representing an uploaded reel
package com.example.instagramclone.Models

// Class representing an uploaded reel
class UploadReel {

    // Mutable properties to store reel URL, caption, and profile link
    var uploadReelUrl: String = "" // URL of the uploaded reel
    var caption: String = "" // Caption associated with the reel
    var profileLink: String? = null // Profile link of the uploader

    // Default constructor with no parameters
    constructor()

    // Secondary constructor with parameters to initialize reel URL and caption
    constructor(uploadReelUrl: String, caption: String) {
        this.uploadReelUrl = uploadReelUrl
        this.caption = caption
    }

    // Secondary constructor with parameters to initialize all properties
    constructor(uploadReelUrl: String, caption: String, profileLink: String) {
        this.uploadReelUrl = uploadReelUrl
        this.caption = caption
        this.profileLink = profileLink
    }
}