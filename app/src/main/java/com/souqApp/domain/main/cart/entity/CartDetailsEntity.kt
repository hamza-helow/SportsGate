package com.souqApp.domain.main.cart.entity


data class CartDetailsEntity(
    val subTotal: String,
    val settingCurrency: String,
    val products: List<ProductInCartEntity>,
)