package com.souqApp.data.orders.remote.dto

import com.google.gson.annotations.SerializedName

data class OrderResponse(
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("number")
    val number: String?,
    @SerializedName("total_price")
    val totalPrice: String?,
    @SerializedName("status")
    val status: OrderStatus?,
    @SerializedName("href")
    val href: String?
)