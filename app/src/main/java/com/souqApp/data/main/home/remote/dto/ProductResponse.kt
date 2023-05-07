package com.souqApp.data.main.home.remote.dto

import com.google.gson.annotations.SerializedName
import com.souqApp.data.product_details.remote.Variation

data class ProductEntity(
    @SerializedName("desc")
    val desc: String,
    @SerializedName("discount")
    val discount: Double,
    @SerializedName("thumb")
    val thumb: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("on_sale")
    val onSale: Boolean,
    @SerializedName("percent_added_tax")
    val percentAddedTax: Double,
    @SerializedName("price")
    val price: String,
    @SerializedName("discounted_product_price")
    val discountedProductPrice: String,
    @SerializedName("discount_percentage")
    val discountPercentage: Double,
    @SerializedName("sale_price")
    val salePrice: Double,
    @SerializedName("variations")
    val variations: List<Variation>,
    @SerializedName("variation_compaination_id")
    val variation_compaination_id:Int?,
)