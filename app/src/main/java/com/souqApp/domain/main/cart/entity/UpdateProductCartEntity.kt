package com.souqApp.domain.main.cart.entity

data class UpdateProductCartEntity(
    val cartItemId:Int,
    val subTotal: String,
    val cartProductsCount: Int
)