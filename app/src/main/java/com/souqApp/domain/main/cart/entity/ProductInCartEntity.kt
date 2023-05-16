package com.souqApp.domain.main.cart.entity

data class ProductInCartEntity(
    val id: Int,
    val cartItemId:Int,
    val name: String,
    val thumb: String,
    var totalPrice: String,
    var qty: Int,
    val combinationId: Int?,
)