package com.souqApp.data.main.cart.remote.dto

import com.google.gson.annotations.SerializedName

data class UpdateProductQtyResponse(
    @SerializedName("sub_total") val subTotal: Double,
    @SerializedName("setting_currency") val settingCurrency: String,
    @SerializedName("cart_products_count") val cartProductsCount: String
)