package com.souqApp.data.product_details.remote

import com.google.gson.annotations.SerializedName

data class AddProductToCartResponse(
    @SerializedName("products_count")
    val productsCount: Int?
)