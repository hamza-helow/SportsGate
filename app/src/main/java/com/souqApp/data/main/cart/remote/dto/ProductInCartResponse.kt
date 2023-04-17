package com.souqApp.data.main.cart.remote.dto

import com.google.gson.annotations.SerializedName

data class ProductInCartResponse(
    @SerializedName("cart_id") val cartItemId:Int?,
    @SerializedName("product_id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("thumb") val thumb: String,
    @SerializedName("item_price") val itemPrice: String,
    @SerializedName("total_price") val totalPrice: String,
    @SerializedName("qty") var qty: Int,
    @SerializedName("out_of_stock") val availableInStock: Boolean,
    @SerializedName("combination_id") val combinationId: Int?,
)