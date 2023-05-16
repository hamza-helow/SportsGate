package com.souqApp.data.main.cart.remote.dto

import com.google.gson.annotations.SerializedName

data class UpdateProductCartResponse(
    @SerializedName("cart_id") val cartItemId: Int?,
    @SerializedName("sub_total") val subTotal: String?,
    @SerializedName("updated_qty") val updatedQty: Int?,
    @SerializedName("items_price") val itemsPrice: String?,
)