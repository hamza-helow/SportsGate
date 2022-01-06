package com.souqApp.data.register.remote.dto

import com.google.gson.annotations.SerializedName

data class ActiveAccountRequest(
    @SerializedName("code") val code: String,
    @SerializedName("device_type") val deviceType: String,
    @SerializedName("firebase_token") val firebaseToken: String,
    @SerializedName("Accept-Language") val AcceptLanguage: String,
)