package com.souqApp.data.contact_us.remote

data class ContactUsRequest(
    val name: String,
    val email: String,
    val phone: String,
    val message: String
)