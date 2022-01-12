package com.souqApp.data.main.home.remote.dto

import com.google.gson.annotations.SerializedName

data class HomeResponse(
    @SerializedName("cart_products_count") val cartProductsCount: Int,
    @SerializedName("show_location") val showLocation: Boolean,
    @SerializedName("products_ads") val ads: List<ProductAdsResponse>,
    @SerializedName("categories") val categories: List<CategoryResponse>,
    @SerializedName("new_products") val newProducts: List<ProductResponse>,
    @SerializedName("best_selling_products") val bestSellingProducts: List<ProductResponse>,
    @SerializedName("recommended_products") val recommendedProducts: List<ProductResponse>,
)