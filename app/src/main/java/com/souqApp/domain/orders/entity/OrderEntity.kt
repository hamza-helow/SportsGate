package com.souqApp.domain.orders.entity

data class OrderEntity(
    val createdAt: String,
    val id: Int,
    val number: String,
    val totalPrice: String,
    val orderStatus: OrderStatusEntity?,
    val href: String
)