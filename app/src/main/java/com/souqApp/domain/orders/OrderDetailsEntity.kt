package com.souqApp.domain.orders

data class OrderDetailsEntity(
    val address: String,
    val couponDiscount: String,
    val couponPercent: Double,
    val deliveryOptionId: Int,
    val deliveryPrice: String,
    val orderNumber: String,
    val products: List<ProductInOrderEntity>,
    val reason: String,
    val status: Int,
    val statusDescription: String,
    val subTotal: String,
    val total: String,
    val vat: String,
    val createdAt:String
)


data class ProductInOrderEntity(
    val discountPrice: String,
    val id: Int,
    val lastPrice: String,
    val name: String,
    val qty: Int,
    val thumb: String,
    val total_price: String,
    val variation_compaination_id: Int,
    val variation_compaination_label: String
)