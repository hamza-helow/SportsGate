package com.souqApp.domain.main.cart.entity


data class CheckoutDetailsEntity(
    val userDefaultAddress: String,
    val userDefaultAddressId: Int,
    val subTotal: Double,
    val valueAddedTax: Double,
    val deliveryPrice: Double,
    val couponDiscount: Double,
    val totalPrice: Double,
    val settingCurrency: String,
)