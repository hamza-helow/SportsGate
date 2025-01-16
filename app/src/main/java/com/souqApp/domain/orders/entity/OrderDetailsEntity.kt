package com.souqApp.domain.orders.entity

data class OrderDetailsEntity(
    val address: String,
    val couponPercent: Double,
    val deliveryOptionId: Int,
    val orderNumber: String,
    val products: List<ProductInOrderEntity>,
    val reason: String,
    val status: OrderStatusEntity?,
    val orderSummary: OrderSummaryEntity?,
    val createdAt: String,
    val href:String
)


data class ProductInOrderEntity(
    val discountPrice: String,
    val id: Int,
    val lastPrice: String,
    val name: String,
    val qty: Int,
    val thumb: String,
    val totalPrice: String,
    val variationCompainationId: Int,
    val variationCompainationLabel: String
)