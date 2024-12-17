package com.souqApp.data.orders.remote.dto

import com.google.gson.annotations.SerializedName


data class OrderDetailsResponse(
    @SerializedName("address")
    val address: String?,
    @SerializedName("coupon_percent")
    val couponPercent: Double?,
    @SerializedName("delivery_option_id")
    val deliveryOptionId: Int?,
    @SerializedName("order_number")
    val orderNumber: String?,
    @SerializedName("products")
    val products: List<ProductInOrderResponse>,
    @SerializedName("reason")
    val reason: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("summary")
    val summary: OrderSummary? ,
    @SerializedName("status")
    val status: OrderStatus?
)

data class ProductInOrderResponse(
    @SerializedName("discount_price")
    val discountPrice: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("last_price")
    val lastPrice: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("qty")
    val qty: Int?,
    @SerializedName("thumb")
    val thumb: String?,
    @SerializedName("total_price")
    val total_price: String?,
    @SerializedName("variation_compaination_id")
    val variation_compaination_id: Int?,
    @SerializedName("variation_compaination_label")
    val variation_compaination_label: String?
)