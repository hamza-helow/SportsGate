package com.souqApp.data.main.cart.remote.dto

import com.google.gson.annotations.SerializedName

class CartDetailsResponse(
    @SerializedName("sub_total") val subTotal: String,
    @SerializedName("currency") val currency: String,
    @SerializedName("products") val products: List<ProductInCartResponse>,
)