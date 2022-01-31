package com.souqApp.domain.main.cart.entity

import com.souqApp.data.main.cart.remote.dto.ProductInCartResponse

data class CartDetailsEntity(
    val subTotal: Double,
    val settingCurrency: String,
    val allAvailableInStock: Boolean,
    val products: List<ProductInCartResponse>,
)