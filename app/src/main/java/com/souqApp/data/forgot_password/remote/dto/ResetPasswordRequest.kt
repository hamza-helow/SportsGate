package com.souqApp.data.forgot_password.remote.dto

import com.google.gson.annotations.SerializedName

data class ResetPasswordRequest(
    @SerializedName("phone") val phone: String,

    @SerializedName("reset_code") val resetCode: String,
    )