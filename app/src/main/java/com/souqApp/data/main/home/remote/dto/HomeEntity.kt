package com.souqApp.data.main.home.remote.dto

import com.souqApp.data.main.common.CategoryEntity


data class HomeEntity(
    val cart_products_count: Int,
    val show_location: Boolean,
    val promotions: List<ProductAdsEntity>,
    val categories: List<CategoryEntity>,
    val new_products: List<ProductEntity>,
    val best_selling_products: List<ProductEntity>,
    val recommended_products: List<ProductEntity>,
)