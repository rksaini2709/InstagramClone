package com.example.instagramclone.Models

// User class represents the data model for a user in the Instagram clone app
class User {
    // Properties to hold user information
    var image: String? = null // User profile image URL
    var name: String? = null // User's name
    var email: String? = null // User's email
    var password: String? = null // User's password

    // Default constructor
    constructor()

    // Constructor with all properties
    constructor(image: String?, name: String?, email: String?, password: String?) {
        this.image = image
        this.name = name
        this.email = email
        this.password = password
    }

    // Constructor without image property
    constructor(name: String?, email: String?, password: String?) {
        this.name = name
        this.email = email
        this.password = password
    }

    constructor(email: String?, password: String?) {
        this.email = email
        this.password = password
    }
}
