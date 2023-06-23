package com.souqApp.domain.main.cart.entity


data class CartDetailsEntity(
    var subTotal: String,
    val settingCurrency: String,
    val products: List<ProductInCartEntity>,
    var placeOrderPercentage:Int,
    var isAbleToPlaceOrder:Boolean,
    var placeOrderAmount:String
)