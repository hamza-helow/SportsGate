package com.souqApp.domain.main.cart.entity

import com.google.gson.annotations.SerializedName

data class ProductInCartEntity(
    val id: Int,
    @SerializedName("cart_id")
    val cartItemId:Int,
    val name: String,
    val thumb: String,
    var totalPrice: String,
    var qty: Int,
    val combinationId: Int?,
)