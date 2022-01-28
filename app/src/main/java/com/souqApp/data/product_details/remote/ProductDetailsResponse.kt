package com.souqApp.data.product_details.remote

import com.google.gson.annotations.SerializedName
import com.souqApp.data.main.home.remote.dto.ProductAdsResponse

data class ProductDetailsResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("desc") val desc: String,
    @SerializedName("regular_price") val regularPrice: Double,
    @SerializedName("on_sale") val onSale: Boolean,
    @SerializedName("sale_price") val salePrice: Double,
    @SerializedName("discount") val discount: Double,
    @SerializedName("stock") val stock: Int,
    @SerializedName("user_favourite") val userFavourite: Boolean,
    @SerializedName("percent_added_tax") val percentAddedTax: Double,
    @SerializedName("setting_currency") val settingCurrency: String,
    @SerializedName("setting_percent_added_tax") val settingPercentAddedTax: Double,
    @SerializedName("images") val images: List<ProductAdsResponse>,
    @SerializedName("relevant") val relevant: List<RelevantProductResponse>
)