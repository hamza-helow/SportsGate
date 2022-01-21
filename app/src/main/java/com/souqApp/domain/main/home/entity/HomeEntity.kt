package com.souqApp.domain.main.home.entity

import com.souqApp.data.main.common.CategoryResponse
import com.souqApp.data.main.home.remote.dto.ProductAdsResponse
import com.souqApp.data.main.home.remote.dto.ProductResponse

data class HomeEntity(
    val cartProductsCount: Int,
    val showLocation: Boolean,
    val ads: List<ProductAdsResponse>,
    val categories: List<CategoryResponse>,
    val newProducts: List<ProductResponse>,
    val bestSellingProducts: List<ProductResponse>,
    val recommendedProducts: List<ProductResponse>,
)