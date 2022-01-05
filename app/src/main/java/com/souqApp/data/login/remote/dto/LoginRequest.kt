package com.souqApp.data.login.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email_or_phone") val loginBy: String,
    @SerializedName("password") val password: String,
    @SerializedName("device_type") val deviceType: Int = 0,
    @SerializedName("firebase_token") val firebaseToken: String? = null,
)