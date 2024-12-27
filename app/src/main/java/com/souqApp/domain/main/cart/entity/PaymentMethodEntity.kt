package com.souqApp.domain.main.cart.entity

data class PaymentMethodEntity (
    val id: Int,
    val name: String,
    val label: String,
    val type: String,
    val slug: String,
    val isActive: Boolean,
    val description: String,
)