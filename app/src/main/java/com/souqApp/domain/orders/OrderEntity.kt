package com.souqApp.domain.orders

data class OrderEntity(
    val created_at: String,
    val id: Int,
    val number: String,
    val setting_currency: String,
    val status: String,
    val total_price: Int
)