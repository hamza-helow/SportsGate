package com.souqApp.data.common.remote.dto

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("token")
    val token: String?,
    @SerializedName("verify_phone_required")
    val verifyPhoneRequired: Boolean?,
    @SerializedName("verify_email_required")
    val verifyEmailRequired: Boolean?,
    @SerializedName("verified_phone")
    val verifiedPhone: Boolean?,
    @SerializedName("verified_email")
    val verifiedEmail: Boolean?
)