package com.souqApp.domain.main.cart.entity

class ProductInCartEntity(
    val id: Int,
    val cartItemId:Int,
    val name: String,
    val thumb: String,
    val totalPrice: String,
    var qty: Int,
    val combinationId: Int?,
)