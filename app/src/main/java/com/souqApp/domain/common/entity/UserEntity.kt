package com.souqApp.domain.common.entity

data class UserEntity(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String,
    val image: String,
    val verified: Int,
    val token: String
)