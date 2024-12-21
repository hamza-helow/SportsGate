package com.souqApp.data.settings.remote.dto

import com.google.gson.annotations.SerializedName

class SocialLinksEntity(
    @SerializedName("facebook")
    val facebook: String?,
    @SerializedName("instagram")
    val instagram: String?,
    @SerializedName("tiktok")
    val tiktok: String?,
    @SerializedName("twitter")
    val twitter: String?
)