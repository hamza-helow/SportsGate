package com.souqApp.domain.main.cart.entity

data class UpdateProductQtyEntity(
    val subTotal: Double,
    val settingCurrency: String,
    val cartProductsCount: String
)