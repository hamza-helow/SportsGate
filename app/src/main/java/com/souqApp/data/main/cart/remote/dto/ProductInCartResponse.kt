package com.souqApp.data.main.cart.remote.dto

import com.google.gson.annotations.SerializedName

data class ProductInCartResponse(

    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("first_image") val firstImage: String,
    @SerializedName("piece_price") val piecePrice: Double,
    @SerializedName("quantity_price") val quantityPrice: Int,
    @SerializedName("qty") val qty: Int,
    @SerializedName("stock") val stock: Int,
    @SerializedName("user_favourite") val userFavourite: Boolean,
    @SerializedName("setting_currency") val settingCurrency: String,
    @SerializedName("available_in_stock") val availableInStock: Boolean

)