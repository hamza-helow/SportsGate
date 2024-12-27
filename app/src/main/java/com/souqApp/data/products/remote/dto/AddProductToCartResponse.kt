package com.souqApp.data.products.remote.dto

import com.google.gson.annotations.SerializedName

data class AddProductToCartResponse(
    @SerializedName("products_count")
    val productsCount: Int?
)