package com.souqApp.data.checkout_details.remote.dto

import com.google.gson.annotations.SerializedName
import com.souqApp.data.addresses.remote.dto.AddressResponse
import com.souqApp.domain.addresses.AddressEntity

data class CheckoutDetailsResponse(
    @SerializedName("user_address")
    val userAddress: AddressResponse?,
    @SerializedName("sub_total")
    val subTotal: String,
    @SerializedName("value_added_tax")
    val valueAddedTax: String,
    @SerializedName("delivery_price")
    val deliveryPrice: String,
    @SerializedName("coupon_discount")
    val couponDiscount: String,
    @SerializedName("total_price")
    val totalPrice: String,
    @SerializedName("currency")
    val settingCurrency: String,
    @SerializedName("has_delivery")
    val hasDelivery: Boolean?,
    @SerializedName("has_discount")
    val hasDiscount: Boolean?,
    @SerializedName("has_tax")
    val hasTax: Boolean?,
    @SerializedName("available_delivery_options")
    val availableDeliveryOptions:List<DeliveryOptionResponse>,
    @SerializedName("delivery_option_id")
    val deliveryOptionId:Int?
)

data class DeliveryOptionResponse(
    val id: Int?,
    val name: String?,
    val price: String?
)
