package com.souqApp.domain.main.cart.entity

data class UpdateProductCartEntity(
    val cartItemId:Int,
    val subTotal: String,
    val productQty: Int,
    val itemsPrice: String
)