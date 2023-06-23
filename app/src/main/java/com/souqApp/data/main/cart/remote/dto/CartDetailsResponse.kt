package com.souqApp.data.main.cart.remote.dto

import com.google.gson.annotations.SerializedName

class CartDetailsResponse(
    @SerializedName("sub_total") val subTotal: String,
    @SerializedName("currency") val currency: String,
    @SerializedName("products") val products: List<ProductInCartResponse>,
    @SerializedName("place_order_percentage")
    val placeOrderPercentage:Int?,
    @SerializedName("is_able_to_place_order")
    val isAbleToPlaceOrder:Boolean? ,
    @SerializedName("place_order_amount")
    val placeOrderAmount:String?
)