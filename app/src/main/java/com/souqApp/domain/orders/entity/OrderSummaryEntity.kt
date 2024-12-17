package com.souqApp.domain.orders.entity

data class OrderSummaryEntity(
    val subTotal: String,
    val total: String,
    val deliveryPrice: String,
    val vat: String,
    val couponDiscount: String
)