package com.souqApp.data.orders.remote
import com.google.gson.annotations.SerializedName

data class OrderResponse(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("number")
    val number: String,
    @SerializedName("setting_currency")
    val settingCurrency: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("total_price")
    val totalPrice: Double
)