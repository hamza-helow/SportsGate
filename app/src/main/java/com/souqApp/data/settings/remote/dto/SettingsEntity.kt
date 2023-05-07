package com.souqApp.data.settings.remote.dto

import com.google.gson.annotations.SerializedName

data class SettingsEntity(
    @SerializedName("facebook")
    val facebook: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("instagram")
    val instagram: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("tiktok")
    val tiktok: String,
    @SerializedName("twitter")
    val twitter: String
)