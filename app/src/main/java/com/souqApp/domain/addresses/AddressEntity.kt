package com.souqApp.domain.addresses


data class AddressEntity(
    val id: Int,
    val fullAddress: String,
    val isPrimary: Boolean,
)