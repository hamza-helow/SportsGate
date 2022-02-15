package com.souqApp.domain.products_by_type

import com.souqApp.data.main.home.remote.dto.ProductEntity

data class ProductsByTypeEntity(
    val haseMore: Boolean,
    val products: List<ProductEntity>
)