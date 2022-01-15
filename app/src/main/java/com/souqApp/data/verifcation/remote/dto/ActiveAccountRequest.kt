package com.souqApp.data.verifcation.remote.dto

import com.google.gson.annotations.SerializedName

data class ActiveAccountRequest(
    @SerializedName("code") val code: String,
    @SerializedName("device_type") val deviceType: String,
    @SerializedName("firebase_token") val firebaseToken: String,
)