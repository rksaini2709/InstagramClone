package com.example.instagramclone.Models

class UploadPost {
    var uploadPostUrl: String = ""
    var caption: String = ""
    var uid: String = ""
    var time: String = ""
    var profileLink: String? = null
    var userId: String? = null

    constructor()
    constructor(uploadPostUrl: String, caption: String, name: String, time: String) {
        this.uploadPostUrl = uploadPostUrl
        this.caption = caption
        this.uid = name
        this.time = time
    }

    constructor(uploadPostUrl: String, caption: String, uid: String, time: String, profileLink: String?) {
        this.uploadPostUrl = uploadPostUrl
        this.caption = caption
        this.uid = uid
        this.time = time
        this.profileLink = profileLink
    }

    constructor(uploadPostUrl: String, caption: String, uid: String, time: String, profileLink: String?, userId: String?) {
        this.uploadPostUrl = uploadPostUrl
        this.caption = caption
        this.uid = uid
        this.time = time
        this.profileLink = profileLink
        this.userId = userId
    }
}