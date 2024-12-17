package com.souqApp.data.orders.remote.dto

import com.google.gson.annotations.SerializedName

data class OrderSummary(
    @SerializedName("sub_total")
    val subTotal: String?,
    @SerializedName("total")
    val total: String?,
    @SerializedName("delivery_price")
    val deliveryPrice: String?,
    @SerializedName("vat")
    val vat: String?,
    @SerializedName("coupon_discount")
    val couponDiscount: String?
)