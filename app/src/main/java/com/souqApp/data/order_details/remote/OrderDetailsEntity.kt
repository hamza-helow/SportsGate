package com.souqApp.data.order_details.remote

data class OrderDetailsEntity(
    val coupon_discount: Int,
    val created_at: String,
    val delivery_price: Int,
    val id: Int,
    val number: String,
    val products: List<ProductInOrder>,
    val reason_for_refused: String,
    val setting_currency: String,
    val shipping_address: String,
    val status: String,
    val sub_total: Int,
    val total_price: Int,
    val value_added_tax: Int
)

data class ProductInOrder(
    val first_image: String,
    val id: Int,
    val name: String,
    val piece_price: Int,
    val qty: Int,
    val quantity_price: Int,
    val setting_currency: String
)