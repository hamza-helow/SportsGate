package com.souqApp.domain.checkout_details

import com.souqApp.domain.addresses.AddressEntity


data class CheckoutDetailsEntity(
    val subTotal: String,
    val valueAddedTax: String,
    val deliveryPrice: String,
    val couponDiscount: String,
    val totalPrice: String,
    val settingCurrency: String,
    val userAddress: AddressEntity?,
    val hasDelivery: Boolean,
    val hasDiscount: Boolean,
    val hasTax: Boolean,
    val deliveryOptions: List<DeliveryOptionEntity>,
    val deliveryOptionId:Int
)