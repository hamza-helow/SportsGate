package com.souqApp.domain.main.home

import com.souqApp.data.main.common.CategoryEntity
import com.souqApp.data.main.home.remote.dto.ProductAdsEntity
import com.souqApp.data.main.home.remote.dto.ProductEntity

data class HomeEntity(
    val cartProductsCount: Int,
    val promotions: List<ProductAdsEntity>,
    val categories: List<CategoryEntity>,
    val newProducts: List<ProductEntity>,
    val bestSellingProducts: List<ProductEntity>,
    val recommendedProducts: List<ProductEntity>,
    val tags: List<TagEntity>
)