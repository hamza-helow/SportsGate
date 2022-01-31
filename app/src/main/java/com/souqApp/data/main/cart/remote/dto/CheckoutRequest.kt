package com.souqApp.data.main.cart.remote.dto

import com.google.gson.annotations.SerializedName

class CheckoutRequest(

    @SerializedName("address_id") val addressId: Int,
    @SerializedName("coupon_code") val couponCode: String
)