package com.souqApp.data.orders.remote

import com.google.gson.annotations.SerializedName


data class OrderDetailsResponse(
    @SerializedName("address")
    val address: String?,
    @SerializedName("coupon_discount")
    val couponDiscount: String?,
    @SerializedName("coupon_percent")
    val couponPercent: Double?,
    @SerializedName("delivery_option_id")
    val deliveryOptionId: Int?,
    @SerializedName("delivery_price")
    val deliveryPrice: String?,
    @SerializedName("order_number")
    val orderNumber: String?,
    @SerializedName("products")
    val products: List<ProductInOrderResponse>,
    @SerializedName("reason")
    val reason: String?,
    @SerializedName("status")
    val status: Int?,
    @SerializedName("status_description")
    val statusDescription: String?,
    @SerializedName("sub_total")
    val subTotal: String?,
    @SerializedName("total")
    val total: String?,
    @SerializedName("vat")
    val vat: String? ,
    @SerializedName("created_at")
    val createdAt:String?
)

data class ProductInOrderResponse(
    @SerializedName("discount_price")
    val discountPrice: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("last_price")
    val lastPrice: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("qty")
    val qty: Int?,
    @SerializedName("thumb")
    val thumb: String?,
    @SerializedName("total_price")
    val total_price: String?,
    @SerializedName("variation_compaination_id")
    val variation_compaination_id: Int?,
    @SerializedName("variation_compaination_label")
    val variation_compaination_label: String?
)