package com.souqApp.data.main.home.remote.dto

import com.google.gson.annotations.SerializedName
import com.souqApp.data.main.common.CategoryEntity
import com.souqApp.domain.main.home.TagEntity

data class HomeResponse(
    @SerializedName("cart_products_count")
    val cartProductsCount: Int,
    @SerializedName("promotions")
    val promotions: List<ProductAdsEntity>,
    @SerializedName("categories")
    val categories: List<CategoryEntity>,
    @SerializedName("new_products")
    val newProducts: List<ProductEntity>,
    @SerializedName("best_selling_products")
    val bestSellingProducts: List<ProductEntity>,
    @SerializedName("recommended_products")
    val recommendedProducts: List<ProductEntity>,
    @SerializedName("tags")
    val tags: List<TagEntity>
)