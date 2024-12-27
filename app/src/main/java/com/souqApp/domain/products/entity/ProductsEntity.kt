package com.souqApp.domain.products.entity

import com.souqApp.data.main.home.remote.dto.ProductEntity

class ProductsEntity(
    val products: List<ProductEntity>,
    val currentPage: Int,
    val totalPages: Int

)