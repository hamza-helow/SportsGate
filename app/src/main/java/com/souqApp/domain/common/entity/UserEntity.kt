package com.souqApp.domain.common.entity

data class UserEntity(
    val id: Int?,
    val name: String,
    val email: String,
    val phone: String,
    val image: String,
    val token: String? ,
    val verifyPhoneRequired: Boolean,
    val verifyEmailRequired: Boolean,
    val verifiedPhone: Boolean?,
    val verifiedEmail: Boolean?
)