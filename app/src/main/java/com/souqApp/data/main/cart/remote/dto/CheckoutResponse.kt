package com.souqApp.data.main.cart.remote.dto

import com.google.gson.annotations.SerializedName

data class CheckoutResponse(
    @SerializedName("order_id") val orderId: Int
)