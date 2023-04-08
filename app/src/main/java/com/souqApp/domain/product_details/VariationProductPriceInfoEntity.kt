package com.souqApp.domain.product_details

data class VariationProductPriceInfoEntity(
    val combinationId: Int,
    val discountPercentage: Double,
    val discountPrice: String,
    val lastPrice: String,
    val price: String,
    val qty: Int,
    val currency: String,
)