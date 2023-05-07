package com.souqApp.data.verifcation.remote.dto

import com.google.gson.annotations.SerializedName

data class CreateTokenResetPasswordEntity(
    @SerializedName("token")
    val token: String
)