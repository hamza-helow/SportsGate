package com.souqApp.domain.login.entity

data class LoginEntity(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String,
    val image: String,
    val verified: Int,
    val token: String
)