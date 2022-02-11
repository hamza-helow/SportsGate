package com.souqApp.domain.payment_details


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