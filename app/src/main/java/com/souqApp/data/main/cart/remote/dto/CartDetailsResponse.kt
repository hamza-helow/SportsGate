package com.souqApp.data.main.cart.remote.dto

import com.google.gson.annotations.SerializedName

class CartDetailsResponse(
    @SerializedName("sub_total") val subTotal: Double,
    @SerializedName("setting_currency") val settingCurrency: Double,
    @SerializedName("all_availabe_in_stock") val allAvailableInStock: Boolean,
    @SerializedName("products") val products: List<ProductInCartResponse>,
)