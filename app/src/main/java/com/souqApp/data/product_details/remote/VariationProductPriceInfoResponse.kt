package com.souqApp.data.product_details.remote

import com.google.gson.annotations.SerializedName

data class VariationProductPriceInfoResponse(
    @SerializedName("combination_id")
    val combinationId: Int?,
    @SerializedName("discount_percentage")
    val discountPercentage: Double?,
    @SerializedName("discount_price")
    val discountPrice: String?,
    @SerializedName("last_price")
    val lastPrice: String?,
    val price: String?,
    val qty: Int?,
    val currency: String?,
)