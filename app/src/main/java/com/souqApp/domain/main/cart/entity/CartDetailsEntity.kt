package com.souqApp.domain.main.cart.entity

import com.souqApp.data.main.cart.remote.dto.ProductInCartResponse

data class CartDetailsEntity(
    val subTotal: String,
    val settingCurrency: String,
    val products: List<ProductInCartResponse>,
)