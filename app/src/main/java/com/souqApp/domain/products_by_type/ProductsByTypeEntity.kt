package com.souqApp.domain.products_by_type

import com.souqApp.data.main.home.remote.dto.ProductResponse

data class ProductsByTypeEntity(
    val haseMore: Boolean,
    val products: List<ProductResponse>

)