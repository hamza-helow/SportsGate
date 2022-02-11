package com.souqApp.data.payment_details.remote.dto

import com.google.gson.annotations.SerializedName

data class CheckoutDetailsResponse(
    @SerializedName("user_default_address") val userDefaultAddress: String ,
    @SerializedName("user_default_address_id") val userDefaultAddressId: Int ,
    @SerializedName("sub_total") val subTotal: Double ,
    @SerializedName("value_added_tax") val valueAddedTax: Double ,
    @SerializedName("delivery_price") val deliveryPrice: Double ,
    @SerializedName("coupon_discount") val couponDiscount: Double ,
    @SerializedName("total_price") val totalPrice: Double ,
    @SerializedName("setting_currency") val settingCurrency: String ,
)