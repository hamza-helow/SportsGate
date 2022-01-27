package com.souqApp.data.products_by_type.remote.dto

import com.google.gson.annotations.SerializedName
import com.souqApp.data.main.home.remote.dto.ProductResponse

data class ProductsByTypeResponse(
    @SerializedName("products") val products: List<ProductResponse>,
    @SerializedName("has_more") val hasMore: Boolean,
)